import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.Packet;
import java.io.EOFException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Extractor
{
    private String wireshark_path;
    private String db_path;

    /*
     * wireshark_path - path to a wireshark capture file.
     * db_path        - path to a SQLite database.
     */
    public Extractor(String wireshark_path, String db_path)
    {        
        this.wireshark_path = wireshark_path;
        this.db_path = db_path;
    }

    /*
     * Run the extrator.
     * 
     * Return - true - in case of success,
     *          false - in case of error.
     */
    public boolean run()
    {
        // Open the wireshark capture and the database
        try (
            PcapHandle pcap_handle = Pcaps.openOffline(this.wireshark_path);
            Connection db_connection = DriverManager.getConnection("jdbc:sqlite:" + this.db_path);
            PreparedStatement pstmt = db_connection.prepareStatement("INSERT INTO packets(data) VALUES(?)")
        ) 
        {
            try
            {
                Packet packet;
                
                // Read all packets from the wireshark capture
                while ((packet = pcap_handle.getNextPacketEx()) != null)
                {
                    pstmt.setBytes(1, packet.getRawData());
                    pstmt.addBatch();
                }    
            } 
            catch (EOFException e) // No more packets to read 
            {
                pstmt.executeBatch(); // Insert all packets into the db
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return true;
    } 
}
