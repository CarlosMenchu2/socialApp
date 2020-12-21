package corporation.app.menchus.com.socialmediagamer.models;


public class SliderItem {

    private String imageUrL;
    private long timestamp;

    public  SliderItem(){

    }

    public SliderItem(String imagelrL, long timestamp) {
        this.imageUrL = imagelrL;
        this.timestamp = timestamp;
    }

    public void setImageUrL(String imagelrL) {
        this.imageUrL = imagelrL;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getImageUrL() {
        return imageUrL;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
