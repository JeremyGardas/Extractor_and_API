public class Main
{
    public static void main(String[] args) throws Exception
    {
        // Display arguments when needed
        if (args.length < 2)
        {
            System.out.println("Arguments: [wireshark_capture] [sqlite_db]");
            System.exit(1);
        }

        // Run the extractor
        System.exit((new Extractor(args[0], args[1])).run() ? 0 : 1);
    }
}
