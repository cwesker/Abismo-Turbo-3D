package carviewscenario;

import processing.core.PApplet;
import java.util.ArrayList;

public class Terrain {

    private PApplet p;
    private int mundo;
    public ArrayList<float[]> colliders = new ArrayList<>();
    private ArrayList<float[]> espinas = new ArrayList<>();

    public static final float RAD_ISLA1 = 600f;
    public static final float RAD_ISLA2 = 500f;
    
    private float[] treeX;
    private float[] treeZ;
    private float[] treeH;

    // Configuración de las Montañas del Mundo 1 y 2 (Constantes fijas)
    private static final float MT_X = 0f, MT_Z = 0f;
    private static final float MT_BASE = 180f, MT_HEIGHT = 120f; 

    // Configuración de las 3 Montañas Sucesivas del Mundo 4 (Arreglos)
    private float[] mtX = { 0f, -120f, 120f };
    private float[] mtZ = { 1100f, 650f, 150f };
    private float mtBase = 180f, mtHeight = 110f;

    private float flagX = 0, flagZ = -900;

    public Terrain(PApplet p, int mundo) {
        this.p = p;
        this.mundo = mundo;

        if (mundo == 1) {
            colliders.add(new float[]{MT_X, MT_Z, MT_BASE * 0.6f});
            int cantTrees = 4;
            treeX = new float[cantTrees]; treeZ = new float[cantTrees]; treeH = new float[cantTrees];
            treeX[0] = -420f; treeZ[0] = 750f; treeH[0] = 140f; 
            treeX[1] = -380f; treeZ[1] = 880f; treeH[1] = 155f;
            treeX[2] = 420f;  treeZ[2] = 750f; treeH[2] = 135f; 
            treeX[3] = 380f;  treeZ[3] = 880f; treeH[3] = 150f;

            for (int i = 0; i < cantTrees; i++) {
                colliders.add(new float[]{treeX[i], treeZ[i], treeH[i] * 0.22f});
            }
            flagX = 0; flagZ = -900;
        } 
        else if (mundo == 2) {
            int cantTrees = 6; 
            treeX = new float[cantTrees]; treeZ = new float[cantTrees]; treeH = new float[cantTrees];
            treeX[0] = 422f;  treeZ[0] = 50f;   treeH[0] = 150f; 
            treeX[1] = 0f;    treeZ[1] = -378f; treeH[1] = 150f; 
            treeX[2] = -120f; treeZ[2] = 1000f; treeH[2] = 140f;
            treeX[3] = 120f;  treeZ[3] = 1000f; treeH[3] = 140f;
            treeX[4] = -470f; treeZ[4] = -950f; treeH[4] = 180f;
            treeX[5] = -330f; treeZ[5] = -950f; treeH[5] = 180f;

            for (int i = 0; i < cantTrees; i++) {
                float hitR = (i == 0 || i == 1) ? 6f : treeH[i] * 0.22f;
                colliders.add(new float[]{treeX[i], treeZ[i], hitR});
            }
            flagX = -400f; flagZ = -1050f; 
        }
        else if (mundo == 3) {
            treeX = new float[0]; treeZ = new float[0]; treeH = new float[0];
            flagX = 120f; flagZ = -700f; 
        }
        else if (mundo == 4) {
            treeX = new float[0]; treeZ = new float[0]; treeH = new float[0];
            
            // Agrega hitboxes fijas de las montañas del mundo 4
            for(int i = 0; i < 3; i++) {
                colliders.add(new float[]{mtX[i], mtZ[i], mtBase * 0.5f});
            }

            // Pinchos tridimensionales bien distribuidos en el pasillo ancho
            espinas.add(new float[]{-35f, -300f}); 
            espinas.add(new float[]{ 35f, -420f}); 
            espinas.add(new float[]{-35f, -540f}); 
            espinas.add(new float[]{ 35f, -660f}); 

            flagX = 0f; flagZ = -1900f; 
        }
    }

    public float obtenerAlturaSuelo(float px, float pz) {
        if (mundo == 1) {
            if (px > -40 && px < 40 && pz < 550 && pz > 250) {
                float t = p.map(pz, 550, 250, 0, 1);
                return 35f - (t * 185f); 
            }
            if (PApplet.dist(px, pz, 0, 800) < RAD_ISLA1) return 35f;
            if (PApplet.dist(px, pz, 0, -800) < RAD_ISLA2) return 35f;
        } 
        else if (mundo == 2) {
            if (PApplet.dist(px, pz, 0, 1000) < 300) return 35f; 
            if (PApplet.dist(px, pz, -400, -1000) < 250) return 35f;    

            if (px > -40 && px < 40 && pz >= 300 && pz <= 850) return 35f;   
            if (pz > 260 && pz < 340 && px >= -40 && px <= 440) return 35f;  
            if (px > 360 && px < 440 && pz >= -440 && pz <= 340) return 35f; 
            if (pz > -440 && pz < -360 && px >= -440 && px <= 440) return 35f;
            if (px > -440 && px < -360 && pz >= -850 && pz <= -360) return 35f;
        }
        else if (mundo == 3) {
            if (PApplet.dist(px, pz, 0, 1100) < 120) return 35f;
            if (px > -25 && px < 25 && pz >= 880 && pz <= 980) {
                float t = p.map(pz, 980, 880, 0, 1); return 35f - (t * 50f);
            }
            if (PApplet.dist(px, pz, 0, 500) < 200) return 35f; 
            if (px > -145 && px < -95 && pz >= 220 && pz <= 310) {
                float t = p.map(pz, 310, 220, 0, 1); return 35f - (t * 55f);
            }
            if (PApplet.dist(px, pz, -120, -50) < 110) return 35f; 
            if (PApplet.dist(px, pz, 120, -50) < 110) return 35f;
            if (px > -120 && px < 120 && pz > -100 && pz < 0) return 35f;
            if (px > 95 && px < 145 && pz >= -250 && pz <= -140) {
                float t = p.map(pz, -140, -250, 0, 1); return 35f - (t * 80f);
            }
            if (PApplet.dist(px, pz, 120, -700) < 140) return 35f;
        }
        else if (mundo == 4) {
            if (PApplet.dist(px, pz, 0, 1400) < 100) return 35f;
            if (px > -25 && px < 25 && pz >= 1220 && pz <= 1300) {
                float t = p.map(pz, 1300, 1220, 0, 1); return 35f - (t * 55f);
            }
            if (PApplet.dist(px, pz, -120, 900) < 110) return 35f;
            if (px > -145 && px < -95 && pz >= 770 && pz <= 830) {
                float t = p.map(pz, 830, 770, 0, 1); return 35f - (t * 60f);
            }
            if (PApplet.dist(px, pz, 120, 400) < 110) return 35f;
            if (px > 95 && px < 145 && pz >= 270 && pz <= 330) {
                float t = p.map(pz, 330, 270, 0, 1); return 35f - (t * 60f);
            }
            if (PApplet.dist(px, pz, 0, -100) < 150) return 35f;
            if (px > -65 && px < 65 && pz >= -700 && pz <= -200) return 35f;
            if (PApplet.dist(px, pz, 0, -850) < 120) return 35f;
            if (px > -30 && px < 30 && pz >= -1020 && pz <= -920) {
                float t = p.map(pz, -920, -1020, 0, 1); return 35f - (t * 110f);
            }
            if (PApplet.dist(px, pz, 0, -1850) < 160) return 35f;
        }

        return 160f; 
    }

    public boolean tieneColisionEstructura(float px, float pz) {
        for (float[] c : colliders) {
            float dx = px - c[0];
            float dz = pz - c[1];
            if (dx*dx + dz*dz < c[2]*c[2]) return true;
        }
        return false;
    }

    public boolean tieneColisionEspinas(float px, float pz) {
        if (mundo != 4) return false;
        for (float[] pino : espinas) {
            if (PApplet.dist(px, pz, pino[0], pino[1]) < 32f) return true;
        }
        return false;
    }

    public boolean checkVictoria(float px, float pz) {
        return PApplet.dist(px, pz, flagX, flagZ) < 45f;
    }

    public void display() {
        drawOcean(); 
        dibujarSolYNubes(); 
        
        if (mundo == 1) {
            drawIsland(0, 800, RAD_ISLA1, p.color(110, 150, 60)); 
            drawIsland(0, -800, RAD_ISLA2, p.color(120, 140, 70)); 
            drawRampaDeSalto();
            drawMountain(); // Llama ordenadamente a la versión sin argumentos
            for (int i = 0; i < treeX.length; i++) drawTree(treeX[i], treeZ[i], treeH[i]);
        } 
        else if (mundo == 2) {
            drawIsland(0, 1000, 300, p.color(100, 145, 65));  
            drawIsland(-400, -1000, 250, p.color(105, 150, 70)); 
            drawPuenteZigZag();
            for (int i = 0; i < treeX.length; i++) drawTree(treeX[i], treeZ[i], treeH[i]);
        }
        else if (mundo == 3) {
            drawMundo3Archipielago();
        }
        else if (mundo == 4) {
            drawMundo4GranCircuito();
        }
        
        drawBanderaMeta();
    }

    private void drawMundo4GranCircuito() {
        drawIsland(0, 1400, 100, p.color(100, 150, 65));  
        drawIsland(-120, 900, 110, p.color(110, 145, 60)); 
        drawIsland(120, 400, 110, p.color(105, 140, 55));  
        drawIsland(0, -100, 150, p.color(95, 135, 50));   
        
        p.pushMatrix();
        p.fill(130, 110, 90);
        p.noStroke();
        dibujarTramoPlano(0, -450, 130, 500); 
        
        drawMicroRampa(0, 1300, 1220, 55f, p.color(140, 85, 40));
        drawMicroRampa(-120, 830, 770, 60f, p.color(140, 85, 40));
        drawMicroRampa(120, 330, 270, 60f, p.color(140, 85, 40));
        p.popMatrix();

        drawIsland(0, -850, 120, p.color(115, 150, 65));   
        drawMicroRampa(0, -920, -1020, 110f, p.color(255, 215, 0)); 
        
        drawIsland(0, -1850, 160, p.color(120, 160, 70)); 

        // Bucle del mundo 4: Llama a la versión con argumentos parametrizados
        for (int i = 0; i < 3; i++) {
            drawMountain(mtX[i], mtZ[i], mtBase, mtHeight);
        }

        for (float[] esp : espinas) {
            drawSpike3D(esp[0], esp[1]);
        }
    }

    private void dibujarSolYNubes() {
        p.pushMatrix();
        p.translate(0, -700, -2200); 
        p.rotateY(0.78f); 
        p.fill(255, 215, 100); 
        p.noStroke();
        p.box(150, 150, 150);
        p.popMatrix();

        dibujarNubeProcedimental(-300, -450, 800);  
        dibujarNubeProcedimental(400, -500, 300);   
        dibujarNubeProcedimental(-100, -420, -200); 
        dibujarNubeProcedimental(200, -480, -1100); 
    }

    private void dibujarNubeProcedimental(float x, float y, float z) {
        p.pushMatrix();
        p.translate(x, y, z);
        p.fill(245, 248, 252, 230); 
        p.noStroke();
        p.box(180, 40, 90); 
        p.pushMatrix(); p.translate(15, -25, 0); p.box(100, 35, 70); p.popMatrix(); 
        p.pushMatrix(); p.translate(-60, 10, 10); p.box(70, 30, 60); p.popMatrix();  
        p.pushMatrix(); p.translate(65, 5, -10); p.box(80, 35, 65); p.popMatrix();   
        p.popMatrix();
    }

    private void drawSpike3D(float x, float z) {
        p.pushMatrix();
        p.translate(x, 50, z);
        p.fill(195, 200, 210); 
        p.stroke(50);
        p.strokeWeight(1);
        p.beginShape(PApplet.TRIANGLES);
        p.vertex(0, -25, 0);  p.vertex(-12, 0, -12); p.vertex(12, 0, -12);
        p.vertex(0, -25, 0);  p.vertex(12, 0, -12);  p.vertex(12, 0, 12);
        p.vertex(0, -25, 0);  p.vertex(12, 0, 12);   p.vertex(-12, 0, 12);
        p.vertex(0, -25, 0);  p.vertex(-12, 0, 12);  p.vertex(-12, 0, -12);
        p.endShape();
        p.popMatrix();
    }

    private void drawMundo3Archipielago() {
        drawIsland(0, 1100, 120, p.color(100, 150, 65)); 
        drawIsland(0, 500, 200, p.color(110, 145, 60));   
        drawIsland(-120, -50, 110, p.color(95, 135, 55)); 
        drawIsland(120, -50, 110, p.color(95, 135, 55));
        p.pushMatrix();
        p.translate(0, 50, -50);
        p.rotateX(PApplet.HALF_PI);
        p.fill(95, 135, 55);
        p.noStroke();
        p.rectMode(PApplet.CENTER);
        p.rect(0, 0, 240, 100); 
        p.popMatrix();
        drawIsland(120, -700, 140, p.color(120, 160, 70)); 

        drawMicroRampa(0, 980, 880, 50f, p.color(140, 85, 40));        
        drawMicroRampa(-120, 310, 220, 55f, p.color(140, 85, 40));    
        drawMicroRampa(120, -140, -250, 80f, p.color(140, 85, 40));    
    }

    private void drawMicroRampa(float x, float zStart, float zEnd, float alt, int col) {
        p.pushMatrix();
        p.fill(col);
        p.stroke(40);
        p.beginShape(PApplet.QUADS);
        p.vertex(x - 25, 35, zStart);
        p.vertex(x + 25, 35, zStart);
        p.vertex(x + 25, 35 - alt, zEnd);
        p.vertex(x - 25, 35 - alt, zEnd);
        p.endShape();
        p.popMatrix();
    }

    private void drawOcean() {
        p.pushMatrix();
        p.translate(0, 90, 0);       
        p.rotateX(PApplet.HALF_PI);
        p.noStroke();
        p.fill(10, 45, 125); 
        p.rectMode(PApplet.CENTER);
        p.rect(0, 0, 22000, 22000);
        p.popMatrix();
    }

    private void drawIsland(float x, float z, float r, int col) {
        p.pushMatrix();
        p.translate(x, 50, z);
        p.rotateX(PApplet.HALF_PI);
        p.fill(col);
        p.noStroke();
        p.beginShape();
        for (int i = 0; i < 40; i++) {
            float a = p.TWO_PI * i / 40;
            p.vertex(p.cos(a) * r, p.sin(a) * r);
        }
        p.endShape(p.CLOSE);
        p.popMatrix();
    }

    private void drawRampaDeSalto() {
        p.pushMatrix();
        p.fill(135, 85, 45); 
        p.stroke(40);
        p.beginShape(PApplet.QUADS);
        p.vertex(-40, 35, 550);
        p.vertex(40, 35, 550);
        p.vertex(40, -150, 250); 
        p.vertex(-40, -150, 250);
        p.endShape();
        p.popMatrix();
    }

    private void drawPuenteZigZag() {
        p.pushMatrix();
        p.fill(130, 110, 90); 
        p.noStroke();
        dibujarTramoPlano(0, 575, 80, 550);    
        dibujarTramoPlano(200, 300, 480, 80);  
        dibujarTramoPlano(400, -50, 80, 780);  
        dibujarTramoPlano(0, -400, 880, 80);   
        dibujarTramoPlano(-400, -605, 80, 490); 
        p.popMatrix();
    }

    private void dibujarTramoPlano(float x, float z, float ancho, float profundidad) {
        p.pushMatrix();
        p.translate(x, 50, z);
        p.rotateX(p.HALF_PI);
        p.rectMode(p.CENTER);
        p.rect(0, 0, ancho, profundidad);
        p.popMatrix();
    }

    // ── SOBRECARGA 1: Montaña sin argumentos (Mundos 1 y 2) ────────────────
    private void drawMountain() {
        drawMountain(MT_X, MT_Z, MT_BASE, MT_HEIGHT);
    }

    // ── SOBRECARGA 2: Montaña parametrizada con 4 flotantes (Mundo 4) ──────
    private void drawMountain(float mx, float mz, float base, float height) {
        p.pushMatrix();
        p.translate(mx, 50, mz);
        p.fill(110, 105, 100);
        p.noStroke();
        p.beginShape(PApplet.TRIANGLES);
        int lados = 8;
        for (int i = 0; i < lados; i++) {
            float a1 = p.TWO_PI * i / lados;
            float a2 = p.TWO_PI * (i + 1) / lados;
            p.vertex(0, -height, 0); 
            p.vertex(p.cos(a1) * base, 0, p.sin(a1) * base); 
            p.vertex(p.cos(a2) * base, 0, p.sin(a2) * base); 
        }
        p.endShape();
        p.popMatrix();
    }

    private void drawTree(float x, float z, float h) {
        p.pushMatrix();
        p.translate(x, 50, z);
        float trunkH = h * 0.40f;
        float trunkW = h * 0.08f;
        float canopyS = h * 0.45f;

        p.pushMatrix(); p.translate(h * 0.05f, -0.2f, h * 0.04f); p.rotateX(p.HALF_PI); p.noStroke(); p.fill(15, 35, 75, 120); p.ellipse(0, 0, canopyS * 1.2f, canopyS * 0.8f); p.popMatrix();
        p.fill(105, 65, 35); p.pushMatrix(); p.translate(0, -trunkH * 0.5f, 0); p.box(trunkW, trunkH, trunkW); p.popMatrix();
        p.fill(40, 115, 45); p.pushMatrix(); p.translate(0, -trunkH - canopyS * 0.5f, 0); p.box(canopyS, canopyS, canopyS); p.popMatrix();
        p.popMatrix();
    }

    private void drawBanderaMeta() {
        p.pushMatrix();
        p.translate(flagX, 50, flagZ);
        p.fill(20);
        p.box(6, 120, 6); 
        p.translate(20, -45, 0);
        p.fill(50, 255, 100); 
        p.box(40, 25, 4);
        p.popMatrix();
    }
}