package itm.proyectoharoldo.backend.Utility;

import java.util.Locale;

/**
 * Maps stored schedule values to Spanish labels for API responses (fields still stored as codes in DB).
 */
public final class ScheduleDisplayLabels {

    private ScheduleDisplayLabels() {
    }

    public static String clientTypeLabel(String raw) {
        if (raw == null || raw.isBlank()) {
            return "";
        }
        return switch (raw.trim().toUpperCase(Locale.ROOT)) {
            case "PERSONA", "NATURAL" -> "Persona Natural";
            case "EMPRESA", "BUSINESS" -> "Empresa";
            default -> raw;
        };
    }

    public static String modalityLabel(String raw) {
        if (raw == null || raw.isBlank()) {
            return "";
        }
        return switch (raw.trim().toLowerCase(Locale.ROOT)) {
            case "virtual" -> "Virtual";
            case "presential" -> "Presencial";
            default -> raw;
        };
    }

    public static String statusLabel(String raw) {
        if (raw == null || raw.isBlank()) {
            return "";
        }
        return switch (raw.trim().toUpperCase(Locale.ROOT)) {
            case "CONFIRMED" -> "Confirmada";
            case "CANCELLED", "CANCELED" -> "Cancelada";
            case "PENDING" -> "Pendiente";
            default -> raw;
        };
    }
}
