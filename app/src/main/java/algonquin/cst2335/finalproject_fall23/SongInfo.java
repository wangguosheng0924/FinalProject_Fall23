package algonquin.cst2335.finalproject_fall23;

public class SongInfo {



    public long Id;
    protected String title;

    protected String duration;

    protected String album_name;

    protected String album_cover ;

    public SongInfo(){}

    public SongInfo(String t, String d,String an,String ac
                    ) {
        title = t;
        duration = d;
        album_name = an;
        album_cover = ac;
    }


}
