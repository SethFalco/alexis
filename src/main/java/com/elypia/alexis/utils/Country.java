package com.elypia.alexis.utils;

public enum Country {

    AFGHANISTAN("Afghanistan", "AFG", "AF"),
    ALBANIA("Albania", "ALB", "AL"),
    ALGERIA("Algeria", "DZA","DZ"),
    ARMENIA("Armenia", "ARM", "AM"),
    ARUBA("Aruba", "ABW", "AW"),
    AUSTRALIA("Australia", "AUS", "AU"),
    AUSTRIA("Austria", "AUT", "AT"),
    AZERBAIJAN("Azerbaijan", "AZE", "AZ"),
    BAHRAIN("Bahrain", "BHR", "BH"),
    BELARUS("Belarus", "BLR", "BY"),
    BELGIUM("Belgium", "BEL", "BE"),
    BENGLADESH("Bengladesh", "BGD", "BD"),
    BOSNIA("Bosnia", "BIH", "BA"),
    BULGARIA("Bulgaria", "BGR", "BG"),
    CANADA("Canada", "CAN", "CA"),
    CAMBODIA("Cambodia", "KHM", "KH"),
    CHAD("Chad", "TCD", "TD"),
    CHINA("China", "CHN", "CN"),
    COMOROS("Comoros", "COM", "KM"),
    CROATIA("Croatia", "HRV", "HR"),
    CURACAO("Curacao", "CUW", "CW"),
    CYPRUS("Cyprus", "CYP", "CY"),
    CZECH_REPUBLIC("Czech Republic", "CZE", "CZ"),
    DEMOCRATIC_REPUBLIC_OF_THE_CONGO("Democratic Republic of the Congo", "COD", "CD"),
    DENMARK("Denmark", "DNK", "DK"),
    DJIBOUTI("Djibouti", "DJI", "DJ"),
    EGYPT("Egypt", "EGY", "EG"),
    ERITREA("Eritrea", "ERI", "ER"),
    ESTONIA("Estonia", "EST", "EE"),
    ETHIOPIA("Ethiopia", "ETH", "ET"),
    FAROE_ISLANDS("Faroe Islands", "FRO", "FO"),
    FINLAND("Finland", "FIN", "FI"),
    FRANCE("France", "FRA", "FR"),
    GEORGIA("Georgia", "GEO", "GE"),
    GERMANY("Germany", "DEU", "DE"),
    GREECE("Greece", "GRC", "GR"),
    HUNGARY("Hungary", "HUN", "HU"),
    HONG_KONG("Hong Kong", "HKG", "HK"),
    ICELAND("Iceland", "ISL", "IS"),
    INDIA("India", "IND", "IN"),
    INDONESIA("Indonesia", "IDN", "ID"),
    IRAN("Iran", "IRN", "IR"),
    IRAQ("Iraq", "IRQ", "IQ"),
    IRELAND("Republic of Ireland", "IRL", "IE"),
    ISRAEL("Israel", "ISR", "IL"),
    ITALY("Italy", "ITA", "IT"),
    JAPAN("Japan", "JPN", "JP"),
    JORDAN("Jordan", "JOR", "JO"),
    KAZAKHSTAN("Kazakhstan", "KAZ", "KZ"),
    KENYA("Kenya", "KEN", "KE"),
    KUWAIT("Kuwait", "KWT", "KW"),
    KYRGYZSTAN("Kyrgyzstan", "KGZ", "KG"),
    LEBANON("Lebanon", "LBN", "LB"),
    LIBYA("Libya", "LBY", "LY"),
    LIECHTENSTEIN("Liechtenstein", "LIE", "LI"),
    LITHUANIA("Lithuania", "LTU", "LT"),
    LUXEMBOURG("Luxembourg", "LUX", "LU"),
    MACAU("Macau", "MAC", "MO"),
    MADAGASCAR("Madagascar", "MDG", "MG"),
    MALAWI("Malawi", "MWI", "MW"),
    MAURITANIA("Mauritania", "MRT", "MR"),
    MOLDOVA("Moldova", "MDA", "MD"),
    MOROCCO("Morocco", "MAR", "MA"),
    NAMBIA("Nambia", "NAM", "NA"),
    NETHERLANDS("Netherlands", "NLD", "NL"),
    NEW_ZEALAND("New Zealand", "NZL", "NZ"),
    NEPAL("Nepal", "NPL", "NP"),
    NIGER("Niger", "NER", "NE"),
    NIGERIA("Nigeria", "NGA", "NG"),
    NORTH_KOREA("North Korea", "PRK", "KP"),
    NORWAY("Norway", "NOR", "NO"),
    OMAN("Oman", "OMN", "OM"),
    PALESTINE("State of Palestine", "PSE", "PS"),
    PAKISTAN("Pakistan", "PAK", "PK"),
    PHILIPPINES("Philippines", "PHL", "PH"),
    POLAND("Poland", "POL", "PL"),
    QATAR("Qatar", "QAT", "QA"),
    ROMANIA("Romania", "ROU", "RO"),
    RUSSIA("Russia", "RUS", "RU"),
    RWANDA("Rewanda", "RWA", "RW"),
    SAUDI_ARABIA("Saudi Arabia", "SAU", "SA"),
    SAOMA("Samoa", "WSM", "WS"),
    SERBIA("Serbia", "SRB", "RS"),
    SINGAPORE("Singapore", "SGP", "SG"),
    SINT_MAARTEN("Sint Maarten", "SXM", "SX"),
    SLOVAKIA("Slovakia", "SVK", "SK"),
    SLOVENIA("Slovenia", "SVN", "SI"),
    SOMALIA("Somalia", "SOM", "SO"),
    SOUTH_AFRICA("South Africa", "ZAF", "ZA"),
    SOUTH_KOREA("South Korea", "KOR", "KR"),
    SPAIN("Spain", "ESP", "ES"),
    SRI_LANKA("Sri Lanka", "LKA", "LK"),
    SUDAN("Sudan", "SDN", "SD"),
    SURINAME("Suriname", "SUR", "SR"),
    SWEDEN("Sweden", "SWE", "SE"),
    SWITZERLAND("Switzerland", "CHE", "CH"),
    SYRIA("Syria", "SYR", "SY"),
    TAIWAN("Taiwan", "TWN", "TW"),
    TAJIKISTAN("Tajikistan", "TJK", "TJ"),
    TANZANIA("Tanzania", "TZA", "TZ"),
    TUNISIA("Tunisia", "TUN", "TN"),
    TURKEY("Turkey", "TUR", "TR"),
    UNITED_ARAB_EMIRATES("United Arab Emirates", "ARE", "AE"),
    UNITED_KINGDOM("United Kingdom", "GBR", "GB"),
    UNITED_STATES("United States of America", "USA", "US"),
    UGANDA("Uganda", "UGA", "UG"),
    UKRAINE("Ukraine", "UKR", "UA"),
    UZBEKISTAN("Uzbekistan", "UZB", "UZ"),
    VIETNAM("Vietnam", "VNM", "VN"),
    YEMEN("Yemen", "YEM", "YE"),
    ZIMBABWE("Zimbabwe", "ZWE", "ZW");

    /**
     * The human readable name of this country.
     */
    private final String COUNTRY;

    /**
     * The 3 letter ISO code for this country.
     */
    private final String ISO_CODE;

    /**
     * The 2 letter ISO code for this country.
     */
    private final String ISO_3166;

    Country(String country, String isoCode, String iso3166) {
        COUNTRY = country;
        ISO_CODE = isoCode;
        ISO_3166 = iso3166;
    }

    public String getCountryName() {
        return COUNTRY;
    }

    public String getIsoCode() {
        return ISO_CODE;
    }

    public String getIso3166() {
        return ISO_3166;
    }

    public String getUnicodeEmote() {
        return ChatUtils.replaceWithIndictors(ISO_3166);
    }

    public static Country get(String value) {
        for (Country c : values()) {
            if (c.COUNTRY.equalsIgnoreCase(value) || c.ISO_3166.equalsIgnoreCase(value) ||
                    c.ISO_CODE.equalsIgnoreCase(value) || c.getUnicodeEmote().equalsIgnoreCase(value))
                return c;
        }

        return null;
    }
}
