package fun.whmy.adminfast.admin.utils;

public class PublicKeyMap {
    private String modulus;
    private String exponent;

    public String getModulus() {
        return this.modulus;
    }

    public void setModulus(String modulus) {
        this.modulus = modulus;
    }

    public String getExponent() {
        return this.exponent;
    }

    public void setExponent(String exponent) {
        this.exponent = exponent;
    }

    public String toString() {
        return "PublicKeyMap [modulus=" + this.modulus + ", exponent=" + this.exponent + "]";
    }
}
