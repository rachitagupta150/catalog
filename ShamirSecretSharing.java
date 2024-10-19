import org.json.JSONObject;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ShamirSecretSharing {

    // Class to hold the points (x, y)
    static class Point {
        int x;
        BigInteger y;

        public Point(int x, BigInteger y) {
            this.x = x;
            this.y = y;
        }
    }

    // Function to decode y-values based on their base
    public static BigInteger decodeValue(String base, String value) {
        int baseValue = Integer.parseInt(base);
        return new BigInteger(value, baseValue);
        
    }

    // Function to calculate Lagrange Interpolation and get the constant term
    public static BigInteger lagrangeInterpolation(List<Point> points) {
        BigInteger result = BigInteger.ZERO;

        for (int i = 0; i < points.size(); i++) {
            BigInteger term = points.get(i).y;
            BigInteger denominator = BigInteger.ONE;
            BigInteger numerator = BigInteger.ONE;

            for (int j = 0; j < points.size(); j++) {
                if (i != j) {
                    numerator = numerator.multiply(BigInteger.valueOf(-points.get(j).x));
                    denominator = denominator.multiply(BigInteger.valueOf(points.get(i).x - points.get(j).x));
                }
            }

            term = term.multiply(numerator).divide(denominator);
            result = result.add(term);
        }

        return result;
    }

    // Function to parse the JSON input and calculate the secret
    public static BigInteger calculateSecret(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);

        // Read keys (n and k)
        JSONObject keys = jsonObject.getJSONObject("keys");
        int n = keys.getInt("n");
        int k = keys.getInt("k");

        // Read points (x, y pairs)
        List<Point> points = new ArrayList<>();

        for (String key : jsonObject.keySet()) {
            if (!key.equals("keys")) {
                int x = Integer.parseInt(key);
                JSONObject pointData = jsonObject.getJSONObject(key);
                String base = pointData.getString("base");
                String value = pointData.getString("value");
                BigInteger y = decodeValue(base, value);

                points.add(new Point(x, y));
            }
        }

        // Perform Lagrange Interpolation to find the constant term c
        return lagrangeInterpolation(points);
    }

    public static void main(String[] args) {
        // Sample JSON input (Test case 1)
        String jsonString1 = "{\n" +
                "    \"keys\": {\n" +
                "        \"n\": 4,\n" +
                "        \"k\": 3\n" +
                "    },\n" +
                "    \"1\": {\n" +
                "        \"base\": \"10\",\n" +
                "        \"value\": \"4\"\n" +
                "    },\n" +
                "    \"2\": {\n" +
                "        \"base\": \"2\",\n" +
                "        \"value\": \"111\"\n" +
                "    },\n" +
                "    \"3\": {\n" +
                "        \"base\": \"10\",\n" +
                "        \"value\": \"12\"\n" +
                "    },\n" +
                "    \"6\": {\n" +
                "        \"base\": \"4\",\n" +
                "        \"value\": \"213\"\n" +
                "    }\n" +
                "}";

        // Sample JSON input (Test case 2)
        String jsonString2 = "{\n" +
                "\"keys\": {\n" +
                "    \"n\": 10,\n" +
                "    \"k\": 7\n" +
                "  },\n" +
                "  \"1\": {\n" +
                "    \"base\": \"6\",\n" +
                "    \"value\": \"13444211440455345511\"\n" +
                "  },\n" +
                "  \"2\": {\n" +
                "    \"base\": \"15\",\n" +
                "    \"value\": \"aed7015a346d63\"\n" +
                "  },\n" +
                "  \"3\": {\n" +
                "    \"base\": \"15\",\n" +
                "    \"value\": \"6aeeb69631c227c\"\n" +
                "  },\n" +
                "  \"4\": {\n" +
                "    \"base\": \"16\",\n" +
                "    \"value\": \"e1b5e05623d881f\"\n" +
                "  },\n" +
                "  \"5\": {\n" +
                "    \"base\": \"8\",\n" +
                "    \"value\": \"316034514573652620673\"\n" +
                "  },\n" +
                "  \"6\": {\n" +
                "    \"base\": \"3\",\n" +
                "    \"value\": \"2122212201122002221120200210011020220200\"\n" +
                "  },\n" +
                "  \"7\": {\n" +
                "    \"base\": \"3\",\n" +
                "    \"value\": \"20120221122211000100210021102001201112121\"\n" +
                "  },\n" +
                "  \"8\": {\n" +
                "    \"base\": \"6\",\n" +
                "    \"value\": \"20220554335330240002224253\"\n" +
                "  },\n" +
                "  \"9\": {\n" +
                "    \"base\": \"12\",\n" +
                "    \"value\": \"45153788322a1255483\"\n" +
                "  },\n" +
                "  \"10\": {\n" +
                "    \"base\": \"7\",\n" +
                "    \"value\": \"1101613130313526312514143\"\n" +
                "  }\n" +
                "}";

        // Calculate and print the secrets for both test cases
        BigInteger secret1 = calculateSecret(jsonString1);
        BigInteger secret2 = calculateSecret(jsonString2);

        System.out.println("Secret for Test Case 1: " + secret1);
        System.out.println("Secret for Test Case 2: " + secret2);
    }
}
