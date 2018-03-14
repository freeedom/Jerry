package jerry.connector;


public enum HttpStatus {
    OK("OK",200),Not_Found("Not Found",404),Internal_Server_Error("Internal Server Error",500);
    private String name;
    private int status;

    private HttpStatus(String name, int status)
    {
        this.name = name;
        this.status = status;
    }


    public String getName()
    {
        return name;
    }

    public int getStatus()
    {
        return status;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public static HttpStatus getHttpStatus(int status)
    {
        for(HttpStatus s:HttpStatus.values())
        {
            if(s.getStatus()==status)
            {
                return s;
            }
        }
        return null;
    }
}
