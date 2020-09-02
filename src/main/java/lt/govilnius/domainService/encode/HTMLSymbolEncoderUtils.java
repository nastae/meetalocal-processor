package lt.govilnius.domainService.encode;

public class HTMLSymbolEncoderUtils {

    public static String encode(String s) {
        return s.replaceAll("ą", "&#261;")
                .replaceAll("č", "&#269;")
                .replaceAll("ę", "&#281;")
                .replaceAll("ė", "&#279;")
                .replaceAll("į", "&#303;")
                .replaceAll("š", "&#353;")
                .replaceAll("ų", "&#371;")
                .replaceAll("ū", "&#363;")
                .replaceAll("ž", "&#382;")
                .replaceAll("Ą", "&#260;")
                .replaceAll("Č", "&#268;")
                .replaceAll("Ę", "&#280;")
                .replaceAll("Ė", "&#278;")
                .replaceAll("Į", "&#302;")
                .replaceAll("Š", "&#352;")
                .replaceAll("Ų", "&#370;")
                .replaceAll("Ū", "&#362;")
                .replaceAll("Ž", "&#381;");
    }
}
