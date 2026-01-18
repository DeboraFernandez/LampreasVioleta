package app;

import dao.*;
import model.*;
import services.JsonExporter;
import services.JsonIO;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Demo por consola:
 * - Permite probar rápidamente los DAO (listar, insertar, buscar por id)
 * - Exporta/importa JSON con una instantánea de todas las entidades

 * Ideal para comprobar que:
 * - conexión JDBC funciona
 * - DAOs funcionan
 * - BD tiene integridad (FK)
 */
public class DemoRelaciones {

    // Ruta del JSON de exportación/importación
    private static final File JSON_FILE = new File("data", "lampreasvioleta_export.json");

    // DAOs
    private static final ClienteDAO clienteDAO = new ClienteDAO();
    private static final DetalleClienteDAO detalleClienteDAO = new DetalleClienteDAO();

    private static final ProductoDAO productoDAO = new ProductoDAO();
    private static final PedidoDAO pedidoDAO = new PedidoDAO();
    private static final DetallePedidoDAO detallePedidoDAO = new DetallePedidoDAO();

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {

            while (true) {
                mostrarMenu();
                System.out.print("Opción: ");
                String op = sc.nextLine().trim();

                try {
                    switch (op) {
                        // -------------------- CLIENTE --------------------
                        case "1" -> listarClientes();
                        case "2" -> insertarCliente(sc);
                        case "3" -> buscarClientePorId(sc);

                        // ----------------- DETALLE_CLIENTE ----------------
                        case "4" -> listarDetallesCliente();
                        case "5" -> insertarDetalleCliente(sc);
                        case "6" -> buscarDetalleClientePorId(sc);

                        // -------------------- PRODUCTO -------------------
                        case "7" -> listarProductos();
                        case "8" -> insertarProducto(sc);
                        case "9" -> buscarProductoPorId(sc);

                        // --------------------- PEDIDO --------------------
                        case "10" -> listarPedidos();
                        case "11" -> insertarPedido(sc);
                        case "12" -> buscarPedidoPorId(sc);

                        // ----------------- DETALLE_PEDIDO ----------------
                        case "13" -> listarDetallesPedido();
                        case "14" -> insertarDetallePedido(sc);
                        //----ACTUALIZO SWITCH PARA CAPTURAR LAS NUEVAS OPCIONES
                        case "15" -> listarComerciales();
                        case "16" -> insertarComercial(sc);
                        case "17" -> buscarComercialPorId(sc);
                        case "18" -> listarRepartidores();
                        case "19" -> insertarRepartidor(sc);
                        case "20" -> buscarRepartidorPorId(sc);

                        // ---------------- JSON EXPORT / IMPORT ------------
                        case "21" -> exportarJson();
                        case "22" -> importarJson();
                        //------------AÑADO A REPARTIDOR Y COMERCIAL---------
                        case "23" -> exportarRepartidores();
                        case "24" -> exportarComerciales();

                        case "0" -> {
                            System.out.println("FIN.");
                            return;
                        }
                        default -> System.out.println("Opción no válida.");
                    }
                } catch (SQLException e) {
                    System.err.println("[SQL ERROR] " + e.getMessage());
                } catch (IOException e) {
                    System.err.println("[IO ERROR] " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("[ERROR] " + e.getMessage());
                    e.printStackTrace();
                }

                System.out.println();
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println("=========================================");
        System.out.println(" DEMO RELACIONES (DAO + JSON)");
        System.out.println("=========================================");
        System.out.println("CLIENTE");
        System.out.println("  1  - Listar clientes");
        System.out.println("  2  - Insertar cliente");
        System.out.println("  3  - Buscar cliente por id");
        System.out.println();
        System.out.println("DETALLE_CLIENTE (1:1)");
        System.out.println("  4  - Listar detalles cliente");
        System.out.println("  5  - Insertar detalle cliente");
        System.out.println("  6  - Buscar detalle cliente por id");
        System.out.println();
        System.out.println("PRODUCTO");
        System.out.println("  7  - Listar productos");
        System.out.println("  8  - Insertar producto");
        System.out.println("  9  - Buscar producto por id");
        System.out.println();
        System.out.println("PEDIDO (1:N con cliente)");
        System.out.println("  10 - Listar pedidos");
        System.out.println("  11 - Insertar pedido");
        System.out.println("  12 - Buscar pedido por id");
        System.out.println();
        System.out.println("DETALLE_PEDIDO (N:M pedido-producto)");
        System.out.println("  13 - Listar detalles pedido");
        System.out.println("  14 - Insertar detalle pedido");
        System.out.println();
        System.out.println("COMERCIAL");
        System.out.println("  15 - Listar comerciales");
        System.out.println("  16 - Insertar comercial");
        System.out.println("  17 - Buscar comercial por id");
        System.out.println();
        System.out.println("REPARTIDOR");
        System.out.println("  18 - Listar repartidores");
        System.out.println("  19 - Insertar repartidor");
        System.out.println("  20 - Buscar repartidor por id");
        System.out.println();
        System.out.println("JSON");
        System.out.println("  21 - Exportar BD a JSON");
        System.out.println("  22 - Importar JSON a BD (INSERT en orden FK)");
        System.out.println();
        System.out.println("  23 - Exportar repartidores a JSON");
        System.out.println("  24 - Exportar comerciales a JSON");
        System.out.println("  0  - Salir");
        System.out.println("=========================================");
    }

    // =========================================================
    // CLIENTE
    // =========================================================

    private static void listarClientes() throws SQLException {
        List<Cliente> list = clienteDAO.findAll();
        System.out.println("CLIENTES: " + list.size());
        list.forEach(System.out::println);
    }

    private static void insertarCliente(Scanner sc) throws SQLException {
        System.out.print("id: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        System.out.print("nombre: ");
        String nombre = sc.nextLine().trim();
        System.out.print("email: ");
        String email = sc.nextLine().trim();

        clienteDAO.insert(new Cliente(id, nombre, email));
        System.out.println("Cliente insertado.");
    }

    private static void buscarClientePorId(Scanner sc) throws SQLException {
        System.out.print("id: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        Cliente c = clienteDAO.findById(id);
        System.out.println(c == null ? "No encontrado." : c);
    }

    // =========================================================
    // DETALLE_CLIENTE
    // =========================================================

    private static void listarDetallesCliente() throws SQLException {
        List<DetalleCliente> list = detalleClienteDAO.findAll();
        System.out.println("DETALLES_CLIENTE: " + list.size());
        list.forEach(System.out::println);
    }

    private static void insertarDetalleCliente(Scanner sc) throws SQLException {
        System.out.print("idCliente (debe existir en cliente): ");
        int id = Integer.parseInt(sc.nextLine().trim());
        System.out.print("direccion: ");
        String dir = sc.nextLine().trim();
        System.out.print("telefono: ");
        String tel = sc.nextLine().trim();
        System.out.print("notas: ");
        String notas = sc.nextLine().trim();

        detalleClienteDAO.insert(new DetalleCliente(id, dir, tel, notas));
        System.out.println("DetalleCliente insertado.");
    }

    private static void buscarDetalleClientePorId(Scanner sc) throws SQLException {
        System.out.print("idCliente: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        DetalleCliente d = detalleClienteDAO.findById(id);
        System.out.println(d == null ? "No encontrado." : d);
    }

    // =========================================================
    // PRODUCTO
    // =========================================================

    private static void listarProductos() throws SQLException {
        List<Producto> list = productoDAO.findAll();
        System.out.println("PRODUCTOS: " + list.size());
        list.forEach(System.out::println);
    }

    private static void insertarProducto(Scanner sc) throws SQLException {
        System.out.print("id: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        System.out.print("nombre: ");
        String nombre = sc.nextLine().trim();
        System.out.print("precio: ");
        double precio = Double.parseDouble(sc.nextLine().trim());

        productoDAO.insert(new Producto(id, nombre, precio));
        System.out.println("Producto insertado.");
    }

    private static void buscarProductoPorId(Scanner sc) throws SQLException {
        System.out.print("id: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        Producto p = productoDAO.findById(id);
        System.out.println(p == null ? "No encontrado." : p);
    }

    // =========================================================
    // PEDIDO
    // =========================================================

    private static void listarPedidos() throws SQLException {
        List<Pedido> list = pedidoDAO.findAll();
        System.out.println("PEDIDOS: " + list.size());

        // Mostrar cada pedido y a continuación sus líneas (si tienes findByPedidoId)
        for (Pedido p : list) {
            System.out.println(p);

            // Si tu DetallePedidoDAO tiene método findByPedidoId:
            List<DetallePedido> lineas = detallePedidoDAO.findByPedidoId(p.getId());
            for (DetallePedido dp : lineas) {
                System.out.println("   -> " + dp);
            }
        }
    }
    //MODIFICO EL INSERTAR PEDIDO PARA INCLUIR A MI COMERCIAL Y REPARTIDOR, PREGUNTA CADA CAMPO EN ORDEN CORRECTO
    private static void insertarPedido(Scanner sc) throws SQLException {
        System.out.print("idPedido: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        System.out.print("clienteId (debe existir): ");
        int clienteId = Integer.parseInt(sc.nextLine().trim());
        System.out.print("comercialId (debe existir): ");
        int comercialId = Integer.parseInt(sc.nextLine().trim());
        System.out.print("repartidorId (debe existir): ");
        int repartidorId = Integer.parseInt(sc.nextLine().trim());
        System.out.print("fecha (YYYY-MM-DD): ");
        LocalDate fecha = LocalDate.parse(sc.nextLine().trim());

        pedidoDAO.insert(new Pedido(id, clienteId, comercialId, repartidorId, fecha));
        System.out.println("Pedido insertado.");
    }

    private static void buscarPedidoPorId(Scanner sc) throws SQLException {
        System.out.print("idPedido: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        Pedido p = pedidoDAO.findById(id);

        if (p == null) {
            System.out.println("No encontrado.");
            return;
        }

        System.out.println(p);
        List<DetallePedido> lineas = detallePedidoDAO.findByPedidoId(p.getId());
        for (DetallePedido dp : lineas) {
            System.out.println("   -> " + dp);
        }
    }

    // =========================================================
    // DETALLE_PEDIDO
    // =========================================================

    private static void listarDetallesPedido() throws SQLException {
        List<DetallePedido> list = detallePedidoDAO.findAll();
        System.out.println("DETALLES_PEDIDO: " + list.size());
        list.forEach(System.out::println);
    }

    private static void insertarDetallePedido(Scanner sc) throws SQLException {
        System.out.print("pedidoId (debe existir): ");
        int pedidoId = Integer.parseInt(sc.nextLine().trim());
        System.out.print("productoId (debe existir): ");
        int productoId = Integer.parseInt(sc.nextLine().trim());
        System.out.print("cantidad: ");
        int cantidad = Integer.parseInt(sc.nextLine().trim());
        System.out.print("precioUnit: ");
        double precioUnit = Double.parseDouble(sc.nextLine().trim());

        detallePedidoDAO.insert(new DetallePedido(pedidoId, productoId, cantidad, precioUnit));
        System.out.println("DetallePedido insertado.");
    }

    //AÑADO COMERCIAL
    private static void listarComerciales() throws SQLException {
        ComercialDAO comercialDAO = new ComercialDAO();
        List<Comercial> list = comercialDAO.findAll();
        System.out.println("COMERCIALES: " + list.size());
        list.forEach(System.out::println);
    }

    private static void insertarComercial(Scanner sc) throws SQLException {
        ComercialDAO comercialDAO = new ComercialDAO();
        System.out.print("id: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        System.out.print("nombre: ");
        String nombre = sc.nextLine().trim();
        System.out.print("zona: ");
        String zona = sc.nextLine().trim();
        System.out.print("telefono: ");
        String telefono = sc.nextLine().trim();
        comercialDAO.insert(new Comercial(id, nombre, zona, telefono));
        System.out.println("Comercial insertado.");
    }

    private static void buscarComercialPorId(Scanner sc) throws SQLException {
        ComercialDAO comercialDAO = new ComercialDAO();
        System.out.print("id: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        Comercial c = comercialDAO.findById(id);
        System.out.println(c == null ? "No encontrado." : c);
    }
    //AÑADO REPARTIDOR
    private static void listarRepartidores() throws SQLException {
        RepartidorDAO repartidorDAO = new RepartidorDAO();
        List<Repartidor> list = repartidorDAO.findAll();
        System.out.println("REPARTIDORES: " + list.size());
        list.forEach(System.out::println);
    }

    private static void insertarRepartidor(Scanner sc) throws SQLException {
        RepartidorDAO repartidorDAO = new RepartidorDAO();
        System.out.print("id: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        System.out.print("nombre: ");
        String nombre = sc.nextLine().trim();
        System.out.print("vehiculo: ");
        String vehiculo = sc.nextLine().trim();
        System.out.print("turno: ");
        String turno = sc.nextLine().trim();
        repartidorDAO.insert(new Repartidor(id, nombre, vehiculo, turno));
        System.out.println("Repartidor insertado.");
    }

    private static void buscarRepartidorPorId(Scanner sc) throws SQLException {
        RepartidorDAO repartidorDAO = new RepartidorDAO();
        System.out.print("id: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        Repartidor r = repartidorDAO.findById(id);
        System.out.println(r == null ? "No encontrado." : r);
    }
    // =========================================================
    // JSON EXPORT / IMPORT
    // =========================================================

    /**
     * Exporta una "foto" de la BD a JSON.
     * Lee todas las tablas y las serializa.
     */
    private static void exportarJson() throws SQLException, IOException {
        AppData data = new AppData();

        data.setClientes(clienteDAO.findAll());
        data.setDetallesCliente(detalleClienteDAO.findAll());
        data.setProductos(productoDAO.findAll());
        data.setPedidos(pedidoDAO.findAll());
        data.setDetallesPedido(detallePedidoDAO.findAll());

        JsonIO.write(JSON_FILE, data);

        System.out.println("Exportado JSON en: " + JSON_FILE.getAbsolutePath());
    }
    //AQUÍ AÑADO MIS EXPORT DE REPARTIDORES Y COMERCIALES
    private static void exportarRepartidores() throws SQLException, IOException {
        RepartidorDAO repartidorDAO = new RepartidorDAO();
        List<Repartidor> repList = repartidorDAO.findAll();
        JsonExporter.exportRepartidores(repList, "repartidores.json");
        System.out.println("Exportados en repartidores.json");
    }

    private static void exportarComerciales() throws SQLException, IOException {
        ComercialDAO comercialDAO = new ComercialDAO();
        List<Comercial> comList = comercialDAO.findAll();
        JsonExporter.exportComerciales(comList, "comerciales.json");
        System.out.println("Exportados en comerciales.json");
    }


    /**
     * Importa JSON a la BD haciendo INSERT en orden correcto por FKs:
     *  1) cliente
     *  2) detalle_cliente
     *  3) producto
     *  4) pedido
     *  5) detalle_pedido
        * IMPORTANTE:
     * - No borra lo existente (si ya hay IDs repetidos, fallará por PK).
     * - En clase podéis añadir luego una opción "vaciar tablas" o "upsert".
     */
    private static void importarJson() throws IOException, SQLException {
        if (!JSON_FILE.exists()) {
            System.out.println("No existe el JSON: " + JSON_FILE.getAbsolutePath());
            return;
        }

        AppData data = JsonIO.read(JSON_FILE, AppData.class);

        // 1) Clientes
        for (Cliente c : data.getClientes()) {
            // Podrías comprobar si existe para evitar error, pero lo dejamos simple:
            // si existe, fallará por PK/unique -> perfecto para explicar integridad.
            clienteDAO.insert(c);
        }

        // 2) Detalles cliente (requieren cliente previo)
        for (DetalleCliente d : data.getDetallesCliente()) {
            detalleClienteDAO.insert(d);
        }

        // 3) Productos
        for (Producto p : data.getProductos()) {
            productoDAO.insert(p);
        }

        // 4) Pedidos (requieren cliente previo)
        for (Pedido pe : data.getPedidos()) {
            pedidoDAO.insert(pe);
        }

        // 5) Detalles pedido (requieren pedido y producto previos)
        for (DetallePedido dp : data.getDetallesPedido()) {
            detallePedidoDAO.insert(dp);
        }

        System.out.println("Importación finalizada.");
    }
}
