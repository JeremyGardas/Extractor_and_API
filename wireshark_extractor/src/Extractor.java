import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.Packet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Extractor
{
    public Extractor()
    {
        
    }

    public boolean run() throws Exception
    {
        System.out.println("abc");
        
        PcapHandle handle = Pcaps.openOffline("../../wireshark.pcapng");
        
        System.out.println("okay");

        try {
            while (true) {
                Packet packet = handle.getNextPacketEx();
                if (packet == null) break;
                // Process packet data here
                System.out.println(packet);
            }
        } finally {
            handle.close();
        }
        
        /*
        return true;

        /*
        String url = "jdbc:sqlite:/path/to/your/database.sqlite"; // Replace with your SQLite database path

        Connection connection = DriverManager.getConnection(url);
        // Prepare and execute SQL INSERT statements to insert packet data into the database
        // PreparedStatement pstmt = connection.prepareStatement("INSERT INTO packets(data) VALUES(?)");
        //pstmt.setBytes(1, packet.getRawData());
        //pstmt.executeUpdate();
        connection.close();
        */

        return true;
    }
}

/*
import org.pcap4j.core.PcapHandle;
import org.pcap4j.packet.Packet;

public class CaptureReader {
    public static void main(String[] args) throws Exception {
        String pcapFile = "your_capture_file.pcap"; // Replace with the actual file path
        PcapHandle handle = PcapHandle.openOffline(pcapFile);

        try {
            while (true) {
                Packet packet = handle.getNextPacketEx();
                if (packet == null) break;
                // Process packet data here
                System.out.println(packet);
            }
        } finally {
            handle.close();
        }
    }
}



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection connect() throws SQLException {
        String url = "jdbc:sqlite:/path/to/your/database.sqlite"; // Replace with your SQLite database path
        return DriverManager.getConnection(url);
    }
}


// Inside the packet processing loop
Connection connection = DatabaseConnection.connect();
// Prepare and execute SQL INSERT statements to insert packet data into the database
// Example: PreparedStatement pstmt = connection.prepareStatement("INSERT INTO packets(data) VALUES(?)");
// pstmt.setBytes(1, packet.getRawData());
// pstmt.executeUpdate();
connection.close();

 */