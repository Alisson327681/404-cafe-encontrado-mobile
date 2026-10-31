package com.cafeencontrado.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import java.util.*;

public class HomeActivity extends Activity {

    private final int COFFEE = Color.rgb(117,69,43);
    private final int DARK = Color.rgb(74,44,32);
    private final int SAND = Color.rgb(245,229,210);
    private final int GOLD = Color.rgb(255,210,155);
    private final int CREAM = Color.rgb(255,249,241);
    private final int WHITE = Color.WHITE;
    private final int MUTED = Color.rgb(138,120,108);
    private final int GREEN = Color.rgb(56,107,74);
    private final int RED = Color.rgb(184,59,50);
    private final int ORANGE = Color.rgb(241,119,21);

    private LinearLayout root,body,top;
    private String screen="Inicio";
    private String currentUser="Usuario";

    private final String[] pcStatus=new String[10];
    private final String[] pcClient=new String[10];

    private final ArrayList<String[]> clients=new ArrayList<>();
    private final ArrayList<String[]> snacks=new ArrayList<>();
    private final ArrayList<String[]> cart=new ArrayList<>();

    private int activePc=-1;
    private int activeClient=-1;
    private int reservationMinutes=60;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs=getSharedPreferences("CafeEncontrado",MODE_PRIVATE);

        currentUser=getIntent().getStringExtra("name");

        if(currentUser==null || currentUser.trim().isEmpty()){
            currentUser="Usuario";
        }

        loadLists();

        for(int i=0;i<10;i++){
            pcStatus[i]=prefs.getString("pcStatus"+i,"Libre");
            pcClient[i]=prefs.getString("pcClient"+i,"");
        }

        show("Inicio");
    }

    private void loadLists(){
        clients.clear();

        String savedClients=prefs.getString("clients","");

        if(savedClients.isEmpty()){
            clients.add(new String[]{"Cliente general","00000000","General"});
        }else{
            String[] rows=savedClients.split("\n");

            for(String row:rows){
                String[] data=row.split("\\|",-1);

                if(data.length>=3){
                    clients.add(data);
                }
            }
        }

        loadSavedSnacks();
    }

    private int dp(float value){
        return (int)(value*getResources().getDisplayMetrics().density);
    }

    private GradientDrawable bg(int color,int radius){
        GradientDrawable drawable=new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radius));
        return drawable;
    }

    private TextView label(String text,int size,int color,boolean bold){
        TextView t=new TextView(this);
        t.setText(text);
        t.setTextSize(size);
        t.setTextColor(color);

        if(bold){
            t.setTypeface(null,Typeface.BOLD);
        }

        return t;
    }

    private LinearLayout column(){
        LinearLayout l=new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        return l;
    }

    private LinearLayout row(){
        LinearLayout l=new LinearLayout(this);
        l.setOrientation(LinearLayout.HORIZONTAL);
        return l;
    }

    private void addText(LinearLayout parent,String text,int size,int color,boolean bold){
        TextView t=label(text,size,color,bold);

        LinearLayout.LayoutParams p=
                new LinearLayout.LayoutParams(-1,-2);

        p.bottomMargin=dp(10);
        parent.addView(t,p);
    }

    private Button button(String text,Runnable action){
        Button b=new Button(this);
        b.setText(text);
        b.setTextColor(WHITE);
        b.setTextSize(14);
        b.setAllCaps(false);
        b.setBackground(bg(ORANGE,18));
        b.setPadding(dp(12),dp(10),dp(12),dp(10));

        b.setOnClickListener(v->action.run());

        return b;
    }

    private void addButton(LinearLayout parent,String text,Runnable action){
        Button b=button(text,action);

        LinearLayout.LayoutParams p=
                new LinearLayout.LayoutParams(-1,-2);

        p.bottomMargin=dp(10);
        parent.addView(b,p);
    }

    private EditText input(String hint){
        EditText e=new EditText(this);
        e.setHint(hint);
        e.setTextSize(14);
        e.setSingleLine(true);
        e.setPadding(dp(14),dp(10),dp(14),dp(10));
        e.setBackground(bg(WHITE,14));

        LinearLayout.LayoutParams p=
                new LinearLayout.LayoutParams(-1,-2);

        p.bottomMargin=dp(10);
        e.setLayoutParams(p);

        return e;
    }

    private void show(String next) {
        screen=next;
        root=column();
        root.setBackgroundColor(CREAM);

        // Ajustar el contenido para que quede debajo de la cámara y la barra de estado.
        getWindow().setStatusBarColor(DARK);
        getWindow().setNavigationBarColor(DARK);
        getWindow().getDecorView().setSystemUiVisibility(0);

        root.setOnApplyWindowInsetsListener((view, insets) -> {
            view.setPadding(
                    0,
                    insets.getSystemWindowInsetTop(),
                    0,
                    insets.getSystemWindowInsetBottom()
            );
            return insets;
        });

        setContentView(root);
        root.requestApplyInsets();

        top=row();
        top.setPadding(dp(18),dp(16),dp(18),dp(16));

        GradientDrawable header=new GradientDrawable();
        header.setColor(DARK);
        header.setCornerRadii(new float[]{
                0,0,0,0,dp(22),dp(22),dp(22),dp(22)
        });

        top.setBackground(header);
        top.setElevation(dp(6));

        LinearLayout brand=column();

        brand.addView(label("☕ 404 CAFÉ ENCONTRADO",16,WHITE,true));
        brand.addView(label(
                next.equals("Inicio")?"Panel principal":"Administración del cibercafé",
                11,GOLD,false
        ));

        top.addView(brand,new LinearLayout.LayoutParams(0,-2,1));

        Button logout=button("Cerrar sesión",()->{
            new AlertDialog.Builder(this)
                    .setTitle("Cerrar sesión")
                    .setMessage("¿Deseas cerrar sesión?")
                    .setNegativeButton("Cancelar",null)
                    .setPositiveButton("Salir",(d,w)->{
                        prefs.edit().remove("logged_in").apply();

                        Intent intent=new Intent(this,MainActivity.class);
                        intent.addFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TOP|
                                        Intent.FLAG_ACTIVITY_NEW_TASK
                        );

                        startActivity(intent);
                        finish();
                    }).show();
        });

        logout.setTextSize(12);
        logout.setPadding(dp(8),dp(6),dp(8),dp(6));
        logout.setMinHeight(dp(40));
        top.addView(logout);

        root.addView(top);

        ScrollView sc=new ScrollView(this);
        body=column();
        body.setPadding(dp(18),dp(20),dp(18),dp(30));
        body.setBackgroundColor(CREAM);

        sc.addView(body);

        root.addView(sc,new LinearLayout.LayoutParams(-1,0,1));

        switch(next){
            case "Inicio":
                home();
                break;

            case "Computadoras":
                pcs();
                break;

            case "Clientes":
                clientsScreen();
                break;

            case "Snacks":
                snacksScreen();
                break;

            case "Catálogo":
                catalog();
                break;

            case "Reservas":
                reservation();
                break;

            case "Cobros":
                checkout();
                break;

            case "Reportes":
                report();
                break;
        }
    }

    private void home(){
        addText(body,"¡Bienvenido, "+currentUser+"!",24,DARK,true);
        addText(body,"Administra tu café desde aquí ☕",14,MUTED,false);

        LinearLayout card=column();
        card.setPadding(dp(18),dp(18),dp(18),dp(18));
        card.setBackground(bg(WHITE,20));

        addText(card,"Panel principal",18,DARK,true);
        addText(card,"Selecciona una opción para comenzar.",13,MUTED,false);

        body.addView(card);

        addButton(body,"💻 Computadoras",()->show("Computadoras"));
        addButton(body,"👥 Clientes",()->show("Clientes"));
        addButton(body,"🍪 Snacks",()->show("Snacks"));
        addButton(body,"📋 Catálogo",()->show("Catálogo"));
        addButton(body,"📅 Reservas",()->show("Reservas"));
        addButton(body,"💵 Cobros",()->show("Cobros"));
        addButton(body,"📊 Reportes",()->show("Reportes"));
    }

    private void pcs(){
        addText(body,"Computadoras",23,DARK,true);
        addText(body,"Administra el estado de las computadoras.",13,MUTED,false);

        for(int i=0;i<10;i++){
            final int index=i;

            LinearLayout card=column();
            card.setPadding(dp(15),dp(12),dp(15),dp(12));
            card.setBackground(bg(WHITE,18));

            addText(card,"Computadora "+(i+1),17,DARK,true);
            addText(card,"Estado: "+pcStatus[i],14,MUTED,false);

            if(!pcClient[i].isEmpty()){
                addText(card,"Cliente: "+pcClient[i],13,MUTED,false);
            }

            if(pcStatus[i].equals("Libre")){
                addButton(card,"Asignar cliente",()->{
                    activePc=index;
                    showClientSelector();
                });
            }else{
                addButton(card,"Liberar computadora",()->{
                    pcStatus[index]="Libre";
                    pcClient[index]="";
                    savePc(index);
                    show("Computadoras");
                });
            }

            LinearLayout.LayoutParams p=
                    new LinearLayout.LayoutParams(-1,-2);

            p.bottomMargin=dp(12);
            body.addView(card,p);
        }
    }

    private void showClientSelector(){
        ArrayList<String> names=new ArrayList<>();

        for(String[] client:clients){
            names.add(client[0]);
        }

        String[] options=names.toArray(new String[0]);

        new AlertDialog.Builder(this)
                .setTitle("Seleccionar cliente")
                .setItems(options,(dialog,which)->{
                    pcStatus[activePc]="Ocupada";
                    pcClient[activePc]=clients.get(which)[0];
                    savePc(activePc);
                    show("Computadoras");
                })
                .setNegativeButton("Cancelar",null)
                .show();
    }

    private void savePc(int index){
        prefs.edit()
                .putString("pcStatus"+index,pcStatus[index])
                .putString("pcClient"+index,pcClient[index])
                .apply();
    }

    private void clientsScreen(){
        addText(body,"Clientes",23,DARK,true);
        addText(body,"Registra y administra tus clientes.",13,MUTED,false);

        addButton(body,"+ Agregar cliente",()->clientDialog(-1));

        for(int i=0;i<clients.size();i++){
            final int index=i;
            String[] client=clients.get(i);

            LinearLayout card=column();
            card.setPadding(dp(15),dp(12),dp(15),dp(12));
            card.setBackground(bg(WHITE,18));

            addText(card,client[0],17,DARK,true);
            addText(card,"Teléfono: "+client[1],13,MUTED,false);
            addText(card,"Tipo: "+client[2],13,MUTED,false);

            addButton(card,"Editar",()->clientDialog(index));

            addButton(card,"Eliminar",()->{
                new AlertDialog.Builder(this)
                        .setTitle("Eliminar cliente")
                        .setMessage("¿Deseas eliminar este cliente?")
                        .setNegativeButton("Cancelar",null)
                        .setPositiveButton("Eliminar",(d,w)->{
                            clients.remove(index);
                            persistClients();
                            show("Clientes");
                        }).show();
            });

            LinearLayout.LayoutParams p=
                    new LinearLayout.LayoutParams(-1,-2);

            p.bottomMargin=dp(12);
            body.addView(card,p);
        }
    }

    private void clientDialog(int index){
        boolean editing=index>=0;

        EditText name=input("Nombre del cliente");
        EditText phone=input("Teléfono");
        EditText type=input("Tipo de cliente");

        if(editing){
            String[] client=clients.get(index);
            name.setText(client[0]);
            phone.setText(client[1]);
            type.setText(client[2]);
        }

        LinearLayout form=column();
        form.setPadding(dp(15),dp(10),dp(15),dp(5));
        form.addView(name);
        form.addView(phone);
        form.addView(type);

        new AlertDialog.Builder(this)
                .setTitle(editing?"Editar cliente":"Nuevo cliente")
                .setView(form)
                .setNegativeButton("Cancelar",null)
                .setPositiveButton("Guardar",(d,w)->{
                    String n=name.getText().toString().trim();
                    String p=phone.getText().toString().trim();
                    String t=type.getText().toString().trim();

                    if(n.isEmpty()){
                        Toast.makeText(this,"Ingresa el nombre",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(t.isEmpty()){
                        t="General";
                    }

                    String[] data={n,p,t};

                    if(editing){
                        clients.set(index,data);
                    }else{
                        clients.add(data);
                    }

                    persistClients();
                    show("Clientes");
                }).show();
    }

    private void persistClients(){
        StringBuilder sb=new StringBuilder();

        for(String[] client:clients){
            if(sb.length()>0){
                sb.append("\n");
            }

            sb.append(client[0]).append("|")
                    .append(client[1]).append("|")
                    .append(client[2]);
        }

        prefs.edit().putString("clients",sb.toString()).apply();
    }

    private void snacksScreen(){
        addText(body,"Snacks",23,DARK,true);
        addText(body,"Administra los productos disponibles.",13,MUTED,false);

        addButton(body,"+ Agregar producto",()->snackDialog(-1));

        for(int i=0;i<snacks.size();i++){
            final int index=i;
            String[] snack=snacks.get(i);

            LinearLayout card=column();
            card.setPadding(dp(15),dp(12),dp(15),dp(12));
            card.setBackground(bg(WHITE,18));

            addText(card,snack[0],17,DARK,true);
            addText(card,"Precio: $"+snack[1],13,MUTED,false);
            addText(card,"Existencias: "+snack[2],13,MUTED,false);

            addButton(card,"Editar producto",()->snackDialog(index));

            addButton(card,"Eliminar producto",()->{
                new AlertDialog.Builder(this)
                        .setTitle("Eliminar producto")
                        .setMessage("¿Deseas eliminar este producto?")
                        .setNegativeButton("Cancelar",null)
                        .setPositiveButton("Eliminar",(d,w)->{
                            snacks.remove(index);
                            saveSnacks();
                            show("Snacks");
                        }).show();
            });

            LinearLayout.LayoutParams p=
                    new LinearLayout.LayoutParams(-1,-2);

            p.bottomMargin=dp(12);
            body.addView(card,p);
        }
    }

    private void snackDialog(int index){
        boolean editing=index>=0;

        EditText name=input("Nombre del producto");
        EditText price=input("Precio");
        EditText stock=input("Cantidad disponible");

        price.setInputType(8194);
        stock.setInputType(2);

        if(editing){
            String[] snack=snacks.get(index);
            name.setText(snack[0]);
            price.setText(snack[1]);
            stock.setText(snack[2]);
        }

        LinearLayout form=column();
        form.setPadding(dp(15),dp(10),dp(15),dp(5));
        form.addView(name);
        form.addView(price);
        form.addView(stock);

        new AlertDialog.Builder(this)
                .setTitle(editing?"Editar producto":"Nuevo producto")
                .setView(form)
                .setNegativeButton("Cancelar",null)
                .setPositiveButton("Guardar",(d,w)->{
                    String n=name.getText().toString().trim();
                    String p=price.getText().toString().trim();
                    String s=stock.getText().toString().trim();

                    if(n.isEmpty() || p.isEmpty() || s.isEmpty()){
                        Toast.makeText(this,"Completa todos los campos",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String[] data={n,p,s};

                    if(editing){
                        snacks.set(index,data);
                    }else{
                        snacks.add(data);
                    }

                    saveSnacks();
                    show("Snacks");
                }).show();
    }

    private void saveSnacks(){
        StringBuilder sb=new StringBuilder();

        for(String[] snack:snacks){
            if(sb.length()>0){
                sb.append("\n");
            }

            sb.append(snack[0]).append("|")
                    .append(snack[1]).append("|")
                    .append(snack[2]);
        }

        prefs.edit().putString("snacks",sb.toString()).apply();
    }

    private void loadSavedSnacks(){
        snacks.clear();

        String saved=prefs.getString("snacks","");

        if(saved.isEmpty()){
            snacks.add(new String[]{"Café","1.00","20"});
            snacks.add(new String[]{"Galletas","0.50","30"});
            snacks.add(new String[]{"Agua","0.75","25"});
            saveSnacks();
        }else{
            String[] rows=saved.split("\n");

            for(String row:rows){
                String[] data=row.split("\\|",-1);

                if(data.length>=3){
                    snacks.add(data);
                }
            }
        }
    }

    private void catalog(){
        addText(body,"Catálogo",23,DARK,true);
        addText(body,"Productos disponibles para venta.",13,MUTED,false);

        for(String[] snack:snacks){
            LinearLayout card=column();
            card.setPadding(dp(15),dp(12),dp(15),dp(12));
            card.setBackground(bg(WHITE,18));

            addText(card,snack[0],17,DARK,true);
            addText(card,"Precio: $"+snack[1],13,MUTED,false);
            addText(card,"Disponibles: "+snack[2],13,MUTED,false);

            addButton(card,"Agregar al carrito",()->{
                cart.add(snack);
                Toast.makeText(this,"Producto agregado",Toast.LENGTH_SHORT).show();
            });

            LinearLayout.LayoutParams p=
                    new LinearLayout.LayoutParams(-1,-2);

            p.bottomMargin=dp(12);
            body.addView(card,p);
        }

        addButton(body,"Ir a cobros",()->show("Cobros"));
    }

    private void reservation(){
        addText(body,"Reservas",23,DARK,true);
        addText(body,"Administra el tiempo de uso de las computadoras.",13,MUTED,false);

        addText(body,"Selecciona una computadora:",15,DARK,true);

        for(int i=0;i<10;i++){
            final int index=i;

            LinearLayout card=column();
            card.setPadding(dp(15),dp(12),dp(15),dp(12));
            card.setBackground(bg(WHITE,18));

            addText(card,"Computadora "+(i+1),17,DARK,true);
            addText(card,"Estado: "+pcStatus[i],13,MUTED,false);

            addButton(card,"Reservar 1 hora",()->{
                if(pcStatus[index].equals("Ocupada")){
                    Toast.makeText(this,"La computadora está ocupada",Toast.LENGTH_SHORT).show();
                    return;
                }

                pcStatus[index]="Reservada";
                pcClient[index]="Reserva de 1 hora";
                savePc(index);

                Toast.makeText(this,"Reserva registrada",Toast.LENGTH_SHORT).show();
                show("Reservas");
            });

            LinearLayout.LayoutParams p=
                    new LinearLayout.LayoutParams(-1,-2);

            p.bottomMargin=dp(12);
            body.addView(card,p);
        }
    }


    private void checkout(){

        addText(body,"Cobros",23,DARK,true);
        addText(body,"Resumen de productos seleccionados.",13,MUTED,false);

        double total=0;

        for(String[] item:cart){
            try{
                total+=Double.parseDouble(item[1]);
            }catch(Exception ignored){}
        }

        addText(body,"Productos en carrito: "+cart.size(),15,DARK,true);

        addText(
                body,
                "Total: $"+String.format(Locale.US,"%.2f",total),
                20,
                ORANGE,
                true
        );

        addText(body,"Selecciona el método de pago:",16,DARK,true);

        String[] paymentMethods={
                "Efectivo",
                "Tarjeta",
                "Transferencia",
                "QR (Demostración)"
        };

        Spinner paymentSpinner=new Spinner(this);

        ArrayAdapter<String> adapter=new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                paymentMethods
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        paymentSpinner.setAdapter(adapter);

        body.addView(paymentSpinner);

        addButton(body,"Finalizar venta",()->{

            if(cart.isEmpty()){
                Toast.makeText(
                        this,
                        "No hay productos en el carrito",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            double saleTotal=0;

            for(String[] item:cart){
                try{
                    saleTotal+=Double.parseDouble(item[1]);
                }catch(Exception ignored){}
            }

            String paymentMethod=
                    paymentSpinner.getSelectedItem().toString();
            final double totalVenta = saleTotal;
            if(paymentMethod.equals("QR (Demostración)")){

                TextView qrDemo=label(
                        "▦",
                        110,
                        DARK,
                        true
                );

                qrDemo.setGravity(Gravity.CENTER);
                qrDemo.setBackground(bg(WHITE,15));
                qrDemo.setPadding(dp(30),dp(15),dp(30),dp(15));

                LinearLayout qrContent=column();
                qrContent.setPadding(dp(15),dp(10),dp(15),dp(5));

                qrContent.addView(qrDemo);

                TextView warning=label(
                        "QR SIMULADO\n\nEste código no recibe dinero.\nEs solamente una demostración.",
                        14,
                        MUTED,
                        true
                );

                warning.setGravity(Gravity.CENTER);
                warning.setPadding(dp(5),dp(15),dp(5),dp(15));

                qrContent.addView(warning);

                new AlertDialog.Builder(this)
                        .setTitle("Pago mediante QR")
                        .setMessage(
                                "Total de la venta: $"+
                                        String.format(Locale.US,"%.2f",saleTotal)
                        )
                        .setView(qrContent)
                        .setNegativeButton("Cancelar",null)
                        .setPositiveButton("Simular pago realizado",(d,w)->{
                            completeSale(totalVenta,paymentMethod);                        })
                        .show();

            }else{
                completeSale(totalVenta, paymentMethod);
            }
        });

        addButton(body,"Volver al catálogo",()->show("Catálogo"));
    }

    private void completeSale(double saleTotal,String paymentMethod){

        double oldTotal=prefs.getFloat("salesTotal",0);
        int oldCount=prefs.getInt("salesCount",0);

        prefs.edit()
                .putFloat(
                        "salesTotal",
                        (float)(oldTotal+saleTotal)
                )
                .putInt("salesCount",oldCount+1)
                .putString("lastPaymentMethod",paymentMethod)
                .putInt(
                        "payments_"+paymentMethod,
                        prefs.getInt("payments_"+paymentMethod,0)+1
                )
                .apply();

        cart.clear();

        Toast.makeText(
                this,
                "Venta registrada correctamente.\nPago: "+paymentMethod,
                Toast.LENGTH_LONG
        ).show();

        show("Cobros");
    }

    private void report(){
        addText(body,"Reportes",23,DARK,true);
        addText(body,"Resumen general del negocio.",13,MUTED,false);

        float total=prefs.getFloat("salesTotal",0);
        int count=prefs.getInt("salesCount",0);

        LinearLayout card=column();
        card.setPadding(dp(18),dp(18),dp(18),dp(18));
        card.setBackground(bg(WHITE,20));

        addText(card,"Ventas realizadas",15,MUTED,false);
        addText(card,String.valueOf(count),28,DARK,true);

        addText(card,"Ingresos acumulados",15,MUTED,false);
        addText(card,"$"+String.format(Locale.US,"%.2f",total),25,ORANGE,true);

        body.addView(card);

        addText(body,"Computadoras",18,DARK,true);

        int occupied=0;

        for(String status:pcStatus){
            if("Ocupada".equals(status) || "Reservada".equals(status)){
                occupied++;
            }
        }

        addText(body,"Ocupadas o reservadas: "+occupied,14,MUTED,false);
        addText(body,"Libres: "+(10-occupied),14,MUTED,false);

        addButton(body,"Actualizar reporte",()->show("Reportes"));
    }

    @Override
    public void onBackPressed(){
        if(!screen.equals("Inicio")){
            show("Inicio");
        }else{
            super.onBackPressed();
        }
    }
}