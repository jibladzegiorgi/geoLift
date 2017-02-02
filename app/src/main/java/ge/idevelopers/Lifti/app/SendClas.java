package ge.idevelopers.Lifti.app;



public class SendClas {
    public double lat;
    public double lng;
    private String phoneNumber;
    public String liftNumber;
    public byte isStuck;

    public SendClas(double lat, double lng, String phoneNumber, String liftNumber, Byte isStuck) {
        this.lat = lat;
        this.lng = lng;
        this.phoneNumber = phoneNumber;
        this.liftNumber = liftNumber;
        this.isStuck = isStuck;

    }
}
