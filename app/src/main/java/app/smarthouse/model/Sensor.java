package app.smarthouse.model;

public class Sensor {
    private String temperature;
    private String humidity;
    private String gase;
    private String presence;
    private String lightIntensity;


    public Sensor(String temperature, String humidity, String gase, String presence, String lightIntensity) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.gase = gase;
        this.presence = presence;
        this.lightIntensity = lightIntensity;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getGase() {
        return gase;
    }

    public void setGase(String gase) {
        this.gase = gase;
    }

    public String getPresence() {
        return presence;
    }

    public void setPresence(String presence) {
        this.presence = presence;
    }

    public String getLightIntensity() {
        return lightIntensity;
    }

    public void setLightIntensity(String lightIntensity) {
        this.lightIntensity = lightIntensity;
    }
}
