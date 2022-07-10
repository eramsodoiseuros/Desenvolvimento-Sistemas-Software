package BL;

import DL.Dados;
import GUI.View;
import UI.UI;

import java.util.ArrayList;
import java.util.List;

public class Robot implements Dados<Robot>{
    private String codeID;
    private Integer ordensFeitas;

    // true = em movimento , false = LIVRE
    private Boolean ativo;
    private Integer posX;
    private Integer posY;

    protected Robot(String codeID) {
        this.codeID= codeID;
        this.ativo = false;
        this.ordensFeitas = 0;
        this.posX = 0;
        this.posY = 1;
    }

    protected Robot(Robot r){
        this.codeID = r.getCodeID();
        this.ativo = r.getAtivo();
        this.ordensFeitas = r.getOrdensFeitas();
        this.posX = r.getPosX();
        this.posY = r.getPosY();
    }

    public Robot(){
    }

    public Robot(List<String> l) {
        this.codeID = l.get(0);
        this.ordensFeitas = Integer.parseInt(l.get(1));
        this.ativo = false;
        this.posX = 0;
        this.posY = 1;
    }

    //momento em que o robo se encontra ligado até pegar/recolher na palete na pos X Y
    public boolean andaParaPalete(Integer[][] mapa, int x, int y){
        boolean did_it  = false;
        if ( ((x == 0 && posX == 1) || (x == 5 && posX == 4)) && posY == y)
            did_it = true;
        else rotate(mapa);
        return did_it;
    }

    //do momento em que o robo tem uma palete e chega a area de carregamento
    public boolean entregaPalete(Integer[][] mapa){
        boolean did_it  = false;
        if (posX == 2 && posY == 6)
            did_it = true;
        else rotate(mapa);
        return did_it;
    }

    //retorna o robo para a posição defaul e desliga o moço
    public boolean takeBreak(Integer[][] mapa){
        boolean did_it  = false;
        if (posX == 1 && posY == 1){
            mapa[1][1] = 0;
            this.ativo = false;
            this.ordensFeitas++; //assumir que robot acaba ordem sempre que retorna ao estado default
            did_it = true;
        } else rotate(mapa);
        return did_it;
    }

    //robo da um passo clockwise
    private void rotate(Integer[][] mapa){
        Integer lastX   = getPosX();
        Integer lastY   = getPosY();

        if (lastX == 1 && lastY < 6) moveState(mapa, lastX, lastY+1);
        else if (lastY == 6 && lastX < 4) moveState(mapa, lastX+1, lastY);
        else if (lastX == 4 && lastY > 1) moveState(mapa, lastX, lastY-1);
        else if (lastY == 1 && lastX > 1) moveState(mapa, lastX-1, lastY);
    }

    //move o robot para a proxima posição, se nao estiver ocupada
    private void moveState(Integer[][] mapa, Integer newPosX, Integer newPosY){
        if (mapa[newPosX][newPosY] != 1){
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                View.alert("ERRO", "O Robot falhou em ficar quieto... Thread Interrupted.");
            }
            mapa[getPosX()][getPosY()]  = 0;
            mapa[newPosX][newPosY]      = 1;
            setPos(newPosX,newPosY);
            //UI.notifica("|->" + codeID + " está na posição (" + posX + ", " + posY + ").");
        }
    }

    //liga o robo para começar a trabalhar, se a posição inicial nao estiver ocupada
    protected boolean startWork(Integer[][] mapa) {
        if (mapa[1][1]!= 1){
            moveState(mapa, 1,1);
            return true;
        }
        return false;
    }

    public String getCodeID() {
        return codeID;
    }

    protected Boolean getAtivo() {
        return ativo;
    }

    public Integer getOrdensFeitas() {
        return ordensFeitas;
    }

    protected Integer getPosX() { return posX; }

    protected Integer getPosY() { return posY; }

    protected void setPosX(Integer x) { this.posX = x; }

    protected void setPosY(Integer y) { this.posY = y; }

    protected void setPos(Integer x, Integer y) {
        setPosX(x);
        setPosY(y);
    }

    protected void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return "Robot:" + codeID + " -> já realizou " + ordensFeitas + " pedidos.";
    }

    public boolean equals(Robot robo){
        return this.getCodeID().equals(robo.getCodeID());
    }

    public Robot clone(){
        return new Robot(this);
    }

    public Dados<Robot> fromRow(final List<String> l) {
        return new Robot(l);
    }

    public List<String> toRow() {
        List<String> r = new ArrayList<>();
        r.add(this.codeID);
        r.add(this.ordensFeitas.toString());
        return r;
    }

}
