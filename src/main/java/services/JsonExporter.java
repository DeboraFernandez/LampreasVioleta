package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;
import model.Repartidor;
import model.Comercial;
// CREO LA NUEVA CLASE JSONEXPORTER PARA IMPLEMENTAR LA EXPORTACIÓN A JSON
public class JsonExporter {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void exportRepartidores(List<Repartidor> lista, String ruta) throws IOException {
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(ruta), lista);
    }

    public static void exportComerciales(List<Comercial> lista, String ruta) throws IOException {
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(ruta), lista);
    }
}

