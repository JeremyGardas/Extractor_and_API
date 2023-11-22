import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.Packet;
import java.io.EOFException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class Extractor
{
    private String wireshark_path;
    private String db_path;
    private PreparedStatement pstmt;

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
        Run the extrator.
    
        Return - true - in case of success,
                 false - in case of error.
    */
    public boolean run()
    {
        try 
        (
            Connection db_connection = this.open_database();
        )
        {
            // Create a prepared statement for insertion
            this.pstmt = db_connection.prepareStatement("INSERT INTO packets(data) VALUES(?)");

            // Read packets
            for (Packet packet : this.read_wireshark_capture())
            {
                // Add the current packet to a prepared statement
                if (! this.add_packet(packet))
                {
                    System.out.println("[ERROR] Adding a packet to a prepared statement");
                    return false;
                }
            }
    
            // Insert packets into the database
            return this.insert_all_packets();            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /*
        Open and read the wireshark capture.
        
        Return - an array of packets - in case of success,
                 null - in case of error.
    */
    private ArrayList<Packet> read_wireshark_capture()
    {
        ArrayList<Packet> all_packets = new ArrayList<>();

        try 
        (
            PcapHandle pcap_handle = Pcaps.openOffline(this.wireshark_path);
        ) 
        {
            try
            {
                Packet packet;
                
                // Read all packets from the wireshark capture
                while ((packet = pcap_handle.getNextPacketEx()) != null)
                {
                    all_packets.add(packet);
                }    
            } 
            catch (EOFException e) // No more packets to read 
            {
                return all_packets;
            }
        }
        catch (Exception e)
        {
            return null;
        }
        
        return null;
    }

    /*
        Open the database (the handle must be closed by yourself).
        
        Return - a handle to the database - in case of success,
                 null - in case of error.
    */
    private Connection open_database()
    {
        try
        {
            return DriverManager.getConnection("jdbc:sqlite:" + this.db_path);
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    /*
        Add a packet to a prepared statement.
        
        packet - a packet to add.

        Return - true - in case of success,
                 false - in case of error.
    */
    private boolean add_packet(Packet packet)
    {
        try
        {
            this.pstmt.setBytes(1, packet.getRawData());
            this.pstmt.addBatch();
            
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
    
    /*
        Insert packets into the database.
        
        Return - true - in case of success,
                 false - in case of error.
    */
    private boolean insert_all_packets()
    {
        try
        {
            this.pstmt.executeBatch();
            
            return false;
        }
        catch (Exception e)
        {
            return true;
        }
    }
}


