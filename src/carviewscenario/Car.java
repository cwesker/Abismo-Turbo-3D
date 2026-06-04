package carviewscenario;

import processing.core.PApplet;

public class Car {

    private PApplet p;
    public float x, y, z;
    public float angle = 0f;

    private float currentSpeed = 0f;
    private static final float MAX_SPEED   = 12f;
    private static final float NITRO_SPEED = 38f; 
    private static final float TURN_SPEED  = 0.05f; 
    
    public float vy = 0f;
    private float gravity = 0.42f; 

    public float nitro = 100f;
    public int colorMode = 0; 

    public boolean noclip = false; 

    private boolean fuegoNitroActivo = false;

    private float wheelRotation = 0f;
    private float steeringAngle = 0f;
    private float bodyRoll      = 0f;

    private Terrain terrain;

    public Car(PApplet p, Terrain terrain, float x, float y, float z) {
        this.p       = p;
        this.terrain = terrain;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void update(boolean w, boolean s, boolean a, boolean d, boolean space) {
        if (!noclip && terrain.tieneColisionEspinas(x, z)) {
            ((CarViewScenario) p).reiniciarNivel();
            return;
        }

        if (a) angle += TURN_SPEED;
        if (d) angle -= TURN_SPEED;

        float targetMaxSpeed = MAX_SPEED;
        if (space && nitro > 0 && w) {
            targetMaxSpeed = NITRO_SPEED;
            nitro -= 0.75f; 
            fuegoNitroActivo = true; 
        } else {
            fuegoNitroActivo = false;
        }

        if (w) currentSpeed = PApplet.lerp(currentSpeed, targetMaxSpeed, 0.1f);
        else if (s) currentSpeed = PApplet.lerp(currentSpeed, -MAX_SPEED * 0.5f, 0.1f);
        else currentSpeed = PApplet.lerp(currentSpeed, 0f, 0.15f);

        float nx = x - PApplet.sin(angle) * currentSpeed;
        float nz = z - PApplet.cos(angle) * currentSpeed;
        wheelRotation += currentSpeed * 0.05f;

        // Noclip
        if (noclip) {
            y = 35f; // Altura flotante fija sobre las islas
            vy = 0f; // Anula por completo el vector de gravedad
            x = nx;  // Avanza de forma directa en colición con las estructuras rígidas
            z = nz;
        } else {
            float alturaSuelo = terrain.obtenerAlturaSuelo(nx, nz);
            if (y < alturaSuelo - 1) { 
                vy += gravity; 
                y += vy;
                x = nx; 
                z = nz;
            } else { 
                y = alturaSuelo;
                vy = 0f;
                
                if (!terrain.tieneColisionEstructura(nx, nz)) {
                    x = nx;
                    z = nz;
                } else {
                    currentSpeed *= -0.3f; 
                }
            }
        }

        if (a) {
            steeringAngle = PApplet.lerp(steeringAngle, -0.4f, 0.15f);
            bodyRoll      = PApplet.lerp(bodyRoll,       0.04f, 0.10f);
        } else if (d) {
            steeringAngle = PApplet.lerp(steeringAngle,  0.4f, 0.15f);
            bodyRoll      = PApplet.lerp(bodyRoll,      -0.04f, 0.10f);
        } else {
            steeringAngle = p.lerp(steeringAngle, 0f, 0.20f);
            bodyRoll      = p.lerp(bodyRoll,      0f, 0.15f);
        }

        if (terrain.checkVictoria(x, z)) {
            CarViewScenario parent = (CarViewScenario) p;
            parent.gameState = 3; 
        }
    }

    public void display() {
        p.pushMatrix();
        p.translate(x, y, z);
        p.rotateY(angle);

        if (y < 65) {
            p.pushMatrix();
            p.translate(8, 14.8f, 12);        
            p.rotateX(PApplet.HALF_PI);
            p.noStroke();
            p.fill(25, 55, 30, 110); 
            p.ellipse(0, 0, 110, 70);
            p.popMatrix();
        }

        // Chasis
        p.pushMatrix();
        p.rotateZ(bodyRoll);
        
        if (colorMode == 0) p.fill(220, 40, 40);       
        else if (colorMode == 1) p.fill(100, 210, 255); 
        else if (colorMode == 2) p.fill(245, 245, 245); 
        else if (colorMode == 3) p.fill(120, 120, 120); 
        
        p.stroke(50);
        p.box(60, 20, 100);

        // Cabina
        p.pushMatrix();
        p.translate(0, -20, -10);
        p.box(45, 20, 50);
        p.popMatrix();
        
        if (fuegoNitroActivo) {
            p.fill(255, 0, 0); 
            p.noStroke();
            p.pushMatrix(); p.translate(-16, 4, 54); p.box(12, 8, 18); p.popMatrix();
            p.pushMatrix(); p.translate(16, 4, 54); p.box(12, 8, 18); p.popMatrix();
        }
        
        p.popMatrix();

        // Ruedas
        p.fill(30);
        p.noStroke();
        p.pushMatrix(); p.translate(-35, 5, -30); p.rotateY(steeringAngle); p.rotateX(wheelRotation); p.box(12, 20, 20); p.popMatrix();
        p.pushMatrix(); p.translate( 35, 5, -30); p.rotateY(steeringAngle); p.rotateX(wheelRotation); p.box(12, 20, 20); p.popMatrix();
        p.pushMatrix(); p.translate(-35, 5,  30); p.rotateX(wheelRotation); p.box(12, 20, 20); p.popMatrix();
        p.pushMatrix(); p.translate( 35, 5,  30); p.rotateX(wheelRotation); p.box(12, 20, 20); p.popMatrix();

        p.popMatrix();
    }
}