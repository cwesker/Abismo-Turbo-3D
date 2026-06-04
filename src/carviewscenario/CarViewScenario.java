package carviewscenario;

import processing.core.PApplet;

public class CarViewScenario extends PApplet {

    Car     miCarro;
    Terrain miTerreno;

    // ── MOTOR DE ESTADOS (4 MUNDOS LIBRES POR DEFECTO) ──────────────────────
    int gameState = 0; 
    int mundoActual = 1; 
    boolean[] mundosDesbloqueados = {true, true, true, true}; 

    // Personalización estética y modos especiales
    int colorSeleccionado = 0; // 0=Rojo, 1=Azul Celeste, 2=Blanco, 3=Gris
    boolean modoNoclip = false; 

    // Controles de juego
    boolean holdW, holdS, holdA, holdD, holdSpace;

    // Cámara órbita
    float camYaw = 0f, camPitch = 0.45f, camRadius = 350f;

    public static void main(String[] args) {
        PApplet.main(CarViewScenario.class.getName());
    }

    @Override
    public void settings() {
        size(1024, 768, P3D);
    }

    @Override
    public void setup() {
        // CORRECCIÓN: Nombre limpio y atractivo en la barra de la ventana
        surface.setTitle("ABISMO TURBO 3D"); 
        reiniciarNivel();
    }

    public void reiniciarNivel() {
        miTerreno = new Terrain(this, mundoActual);
        
        if (mundoActual == 1) {
            miCarro = new Car(this, miTerreno, 0, 35, 800); 
        } else if (mundoActual == 2) {
            miCarro = new Car(this, miTerreno, 0, 35, 1000); 
        } else if (mundoActual == 3) {
            miCarro = new Car(this, miTerreno, 0, 35, 1100); 
        } else if (mundoActual == 4) {
            miCarro = new Car(this, miTerreno, 0, 35, 1400); 
        }
        miCarro.colorMode = colorSeleccionado; 
        miCarro.noclip = modoNoclip; 
        camYaw = 0f;
    }

    @Override
    public void draw() {
        if (gameState == 2 || gameState == 4) {
            ejecutarGameplay(); 
            if (gameState == 4) {
                dibujarMenuPausa(); 
            }
        } else {
            switch (gameState) {
                case 0: dibujarMenuPrincipal(); break;
                case 1: dibujarSeleccionMundos(); break;
                case 3: dibujarPantallaVictoria(); break;
                case 5: dibujarMenuPersonalizar(); break;
            }
        }
    }

    // ── INTERFACES GRÁFICAS EN AZUL CON EL NUEVO NOMBRE ─────────────────────
    void dibujarMenuPrincipal() {
        background(45, 80, 135); 
        stroke(240, 105, 45);
        strokeWeight(4);
        noFill(); 
        rect(20, 20, width - 40, height - 40, 15);

        textAlign(CENTER, CENTER);
        fill(255);
        textSize(55);
        // CORRECCIÓN: Nombre del juego actualizado en el menú
        text("ABISMO TURBO 3D", width/2, height/3); 
        
        fill(240, 105, 45, 100);
        text("ABISMO TURBO 3D", width/2 + 3, height/3 + 3);

        dibujarBoton(width/2 - 120, height/2, 240, 55, "JUGAR", color(50, 160, 95), color(70, 210, 120));
    }

    void dibujarSeleccionMundos() {
        background(45, 80, 135); 
        stroke(255, 200);
        strokeWeight(3);
        noFill(); 
        rect(20, 20, width - 40, height - 40, 15);

        fill(255);
        textAlign(CENTER, CENTER);
        textSize(40);
        text("SELECCIONAR NIVEL", width/2, 100);

        for (int i = 0; i < 4; i++) {
            int x = width / 2 - 330 + (i * 170); 
            int y = height / 2 - 50;
            
            boolean hover = mouseX > x && mouseX < x+150 && mouseY > y && mouseY < y+110;
            fill(30, 55, 95);
            stroke(255, 150);
            if (hover) {
                fill(40, 75, 125);
                stroke(255);
            }
            strokeWeight(2);
            rect(x, y, 150, 110, 10);
            
            fill(255);
            textSize(22);
            text("Mundo " + (i+1), x + 75, y + 40);
            
            fill(i == 3 ? color(255, 75, 75) : color(80, 255, 130)); 
            textSize(13);
            text(i == 3 ? "¡EXTREMO!" : "DISPONIBLE", x + 75, y + 75);
        }

        dibujarBoton(width/2 - 100, height - 120, 200, 45, "VOLVER", color(80, 85, 95), color(120, 125, 135));
    }

    void dibujarPantallaVictoria() {
        background(45, 80, 135); 
        stroke(255, 200);        
        strokeWeight(4);
        noFill(); 
        rect(20, 20, width - 40, height - 40, 15);

        textAlign(CENTER, CENTER);
        fill(255); 
        textSize(55);
        text("¡NIVEL COMPLETADO!", width/2, height/3);
        
        dibujarBoton(width/2 - 150, height/2, 300, 60, "CONTINUAR", color(30, 60, 120), color(50, 90, 180));
    }

    void dibujarMenuPausa() {
        camera(); 
        noLights();
        hint(DISABLE_DEPTH_TEST); 
        
        fill(15, 25, 45, 200); 
        rect(0, 0, width, height);

        fill(35, 65, 110);
        stroke(255, 180);
        strokeWeight(3);
        rect(width/2 - 180, height/2 - 230, 360, 460, 15);

        fill(255);
        textAlign(CENTER, CENTER);
        textSize(35);
        text("PAUSA", width/2, height/2 - 160);

        dibujarBoton(width/2 - 140, height/2 - 100, 280, 45, "VOLVER AL JUEGO", color(50, 140, 200), color(70, 170, 240));
        dibujarBoton(width/2 - 140, height/2 - 30, 280, 45, "SELECCIONAR NIVELES", color(140, 90, 40), color(190, 130, 60));
        dibujarBoton(width/2 - 140, height/2 + 40, 280, 45, "PERSONALIZAR VEHÍCULO", color(110, 60, 140), color(160, 90, 200));
        
        String textoBotonNoclip = modoNoclip ? "NOCLIP: ACTIVADO" : "NOCLIP: DESACTIVADO";
        int colorFondoNoclip = modoNoclip ? color(40, 155, 80) : color(140, 50, 50);
        int colorHoverNoclip = modoNoclip ? color(55, 195, 105) : color(180, 70, 70);
        dibujarBoton(width/2 - 140, height/2 + 110, 280, 45, textoBotonNoclip, colorFondoNoclip, colorHoverNoclip);
        
        hint(ENABLE_DEPTH_TEST);
    }

    void dibujarMenuPersonalizar() {
        background(45, 80, 135); 
        stroke(255, 150);
        strokeWeight(3);
        noFill(); 
        rect(20, 20, width - 40, height - 40, 15);

        textAlign(CENTER, CENTER);
        fill(255);
        textSize(36);
        text("Seleccionar el color del vehículo", width/2, 110); 

        int[] coloresMuestra = {color(220,40,40), color(100,210,255), color(245), color(120)};

        for (int i = 0; i < 4; i++) {
            int x = width/2 - 150;
            int y = 200 + (i * 85);
            
            boolean seleccionado = (colorSeleccionado == i);
            fill(seleccionado ? color(50, 75, 115) : color(30, 50, 85));
            stroke(seleccionado ? color(255) : color(255, 70));
            strokeWeight(seleccionado ? 3 : 1);
            rect(x, y, 300, 55, 10);

            fill(coloresMuestra[i]);
            stroke(50, 150);
            strokeWeight(1);
            ellipse(x + 150, y + 27.5f, 35, 35);
        }

        textAlign(CENTER, CENTER);
        dibujarBoton(width/2 - 100, height - 120, 200, 45, "ACEPTAR", color(50, 150, 90), color(70, 200, 120));
    }

    private void dibujarBoton(float x, float y, float w, float h, String txt, int colBase, int colHover) {
        boolean hover = mouseX > x && mouseX < x+w && mouseY > y && mouseY < y+h;
        fill(hover ? colHover : colBase);
        stroke(255, 70);
        rect(x, y, w, h, 8);
        fill(255);
        textSize(18);
        textAlign(CENTER, CENTER);
        text(txt, x + w/2, y + h/2);
    }

    void ejecutarGameplay() {
        background(45, 105, 185); 

        if (gameState == 2) { 
            miCarro.update(holdW, holdS, holdA, holdD, holdSpace);
            if (miCarro.y > 115) reiniciarNivel(); 
        }

        float diff = miCarro.angle - camYaw;
        while (diff >  PI) diff -= TWO_PI;
        while (diff < -PI) diff += TWO_PI;
        camYaw += diff * 0.07f;

        ambientLight(80, 80, 95);
        directionalLight(255, 200, 120, 1f, 0.5f, -0.7f); 

        float camX = miCarro.x + camRadius * cos(camPitch) * sin(camYaw);
        float camZ = miCarro.z + camRadius * cos(camPitch) * cos(camYaw);
        float camY = miCarro.y - camRadius * sin(camPitch);
        camera(camX, camY, camZ, miCarro.x, miCarro.y, miCarro.z, 0, 1, 0);

        miTerreno.display();
        miCarro.display();

        // ── CAPA INTERFAZ HUD 2D ────────────────────────────────────────────
        camera(); 
        noLights();
        hint(DISABLE_DEPTH_TEST);
        rectMode(CORNER); 
        
        // Botón CONF
        float btnConfX = width - 130;
        float btnConfY = 30;
        float btnConfW = 100;
        float btnConfH = 35;
        boolean hoverConf = mouseX > btnConfX && mouseX < btnConfX+btnConfW && mouseY > btnConfY && mouseY < btnConfY+btnConfH;
        fill(hoverConf ? color(130, 80, 175) : color(90, 50, 125));
        stroke(255, 60);
        rect(btnConfX, btnConfY, btnConfW, btnConfH, 8);
        fill(255);
        textSize(14);
        textAlign(CENTER, CENTER);
        text("CONF", btnConfX + btnConfW/2, btnConfY + btnConfH/2);

        // Barra de Nitro
        fill(0, 150);
        rect(30, 30, 200, 25, 5); 
        fill(255, 60, 60);
        rect(33, 33, map(miCarro.nitro, 0, 100, 0, 194), 19, 3); 
        fill(255);
        textSize(13);
        text("NITRO [ESPACIO]", 130, 41);
        
        if (modoNoclip) {
            fill(255, 255, 0); 
            textSize(13);
            textAlign(LEFT, CENTER);
            text("[MODO NOCLIP ACTIVADO]", 32, 75);
        }
        
        hint(ENABLE_DEPTH_TEST);
    }

    @Override
    public void mousePressed() {
        if (gameState == 0) { 
            if (mouseX > width/2 - 120 && mouseX < width/2 + 120 && mouseY > height/2 && mouseY < height/2 + 55) {
                gameState = 1;
            }
        } 
        else if (gameState == 1) { 
            for (int i = 0; i < 4; i++) {
                int x = width / 2 - 330 + (i * 170);
                int y = height / 2 - 50;
                if (mouseX > x && mouseX < x+150 && mouseY > y && mouseY < y+110) {
                    mundoActual = i + 1;
                    reiniciarNivel();
                    gameState = 2; 
                }
            }
            if (mouseX > width/2 - 100 && mouseX < width/2 + 100 && mouseY > height - 120 && mouseY < height - 75) {
                gameState = 0;
            }
        }
        else if (gameState == 2) { 
            float btnConfX = width - 130;
            float btnConfY = 30;
            if (mouseX > btnConfX && mouseX < btnConfX+100 && mouseY > btnConfY && mouseY < btnConfY+35) {
                gameState = 4; 
            }
        }
        else if (gameState == 3) { 
            if (mouseX > width/2 - 150 && mouseX < width/2 + 150 && mouseY > height/2 && mouseY < height/2 + 60) {
                gameState = 1; 
            }
        }
        else if (gameState == 4) { 
            if (mouseX > width/2 - 140 && mouseX < width/2 + 140 && mouseY > height/2 - 100 && mouseY < height/2 - 55) {
                gameState = 2; 
            }
            if (mouseX > width/2 - 140 && mouseX < width/2 + 140 && mouseY > height/2 - 30 && mouseY < height/2 + 15) {
                gameState = 1; 
            }
            if (mouseX > width/2 - 140 && mouseX < width/2 + 140 && mouseY > height/2 + 40 && mouseY < height/2 + 85) {
                gameState = 5; 
            }
            if (mouseX > width/2 - 140 && mouseX < width/2 + 140 && mouseY > height/2 + 110 && mouseY < height/2 + 155) {
                modoNoclip = !modoNoclip;
                miCarro.noclip = modoNoclip; 
            }
        }
        else if (gameState == 5) { 
            for (int i = 0; i < 4; i++) {
                int x = width/2 - 150;
                int y = 200 + (i * 85);
                if (mouseX > x && mouseX < x+300 && mouseY > y && mouseY < y+50) {
                    colorSeleccionado = i;
                }
            }
            if (mouseX > width/2 - 100 && mouseX < width/2 + 100 && mouseY > height - 120 && mouseY < height - 75) {
                reiniciarNivel(); 
                gameState = 4; 
            }
        }
    }

    @Override 
    public void keyPressed() {
        if (key == ESC) key = 0; 
        if (key == 'w' || key == 'W') holdW = true;
        if (key == 's' || key == 'S') holdS = true;
        if (key == 'a' || key == 'A') holdA = true;
        if (key == 'd' || key == 'D') holdD = true;
        if (key == ' ') holdSpace = true; 
    }

    @Override 
    public void keyReleased() {
        if (key == 'w' || key == 'W') holdW = false;
        if (key == 's' || key == 'S') holdS = false;
        if (key == 'a' || key == 'A') holdA = false;
        if (key == 'd' || key == 'D') holdD = false;
        if (key == ' ') holdSpace = false;
    }
}