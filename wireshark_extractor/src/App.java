public class App
{
    public static void main(String[] args) throws Exception
    {
        System.exit((new Extractor()).run() ? 0 : 1);
    }
}
