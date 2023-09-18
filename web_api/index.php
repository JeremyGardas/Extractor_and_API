<?php
    class API
    {
        private $path_to_database;
        private $db;

        public function __construct($path_to_database)
        {
            $this->path_to_database = $path_to_database;
        }

        /*
            Run the API.
        */
        public function run()
        {
            /*
                The only allowed method is GET.
            */
            if ($_SERVER["REQUEST_METHOD"] !== "GET")
            {
                exit();
            }

            header("Content-Type: application/json");
            
            try
            {
                /*
                    Open the db.
                */
                if (! $this->open_db())
                {
                    throw new Exception();
                }

                /*
                    Read data from db.
                */
                $data = $this->read_data_from_db();

                /*
                    Display data as JSON.
                */
                echo json_encode($data);
            }
            finally
            {
                $this->db->close();
                exit();
            }
        }

        /*
            Open the database.

            Return - true - in case of success,
                     false - in case of error.
        */
        private function open_db() :bool
        {
            $this->db = new SQLite3($this->path_to_database);
            
            return $this->db ? true : false;
        }

        /*
            Read data from db.

            Return - an array containing data - in case of success,
                     an empty array - in case of error.
        */
        private function read_data_from_db() :array
        {
            $result = $this->db->query("SELECT * FROM wireshark");

            if (! $result) {
                return array();
            }
            
            $data = array();

            while ($row = $result->fetchArray(SQLITE3_ASSOC))
            {
                $data[] = $row;
            }

            return $data;
        }
    }

    (new API("../wireshark.sqlite"))->run();
?>