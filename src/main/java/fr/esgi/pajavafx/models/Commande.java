package fr.esgi.pajavafx.models;

import java.sql.Timestamp;

public class Commande {
    private int id;
    private int ticket_id;
    private int technicien_id;
    private String status;
    private Timestamp created_at;

    public Commande() {}

    public Commande(int id, int ticket_id, int technicien_id, String status, Timestamp created_at) {
        this.id = id;
        this.ticket_id = ticket_id;
        this.technicien_id = technicien_id;
        this.status = status;
        this.created_at = created_at;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getTicket_id() { return ticket_id; }
    public void setTicket_id(int ticket_id) { this.ticket_id = ticket_id; }

    public int getTechnicien_id() { return technicien_id; }
    public void setTechnicien_id(int technicien_id) { this.technicien_id = technicien_id; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreated_at() { return created_at; }
    public void setCreated_at(Timestamp created_at) { this.created_at = created_at; }
}
