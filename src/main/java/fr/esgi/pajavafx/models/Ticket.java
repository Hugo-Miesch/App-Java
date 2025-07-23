package fr.esgi.pajavafx.models;

import java.sql.Timestamp;

public class Ticket {
    private int id;
    private int robot_id;
    private int entrepot_id;
    private String issue;
    private String priority;
    private String status;
    private Timestamp created_at;
    private Timestamp resolved_at;
    private int technicien_id;

    public Ticket() {}

    public Ticket(
            int id, int robot_id, int entrepot_id, String issue, String priority,
            String status, Timestamp created_at, Timestamp resolved_at, int technicien_id
    ) {
        this.id = id;
        this.robot_id = robot_id;
        this.entrepot_id = entrepot_id;
        this.issue = issue;
        this.priority = priority;
        this.status = status;
        this.created_at = created_at;
        this.resolved_at = resolved_at;
        this.technicien_id = technicien_id;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getRobot_id() { return robot_id; }
    public void setRobot_id(int robot_id) { this.robot_id = robot_id; }

    public int getEntrepot_id() { return entrepot_id; }
    public void setEntrepot_id(int entrepot_id) { this.entrepot_id = entrepot_id; }

    public String getIssue() { return issue; }
    public void setIssue(String issue) { this.issue = issue; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreated_at() { return created_at; }
    public void setCreated_at(Timestamp created_at) { this.created_at = created_at; }

    public Timestamp getResolved_at() { return resolved_at; }
    public void setResolved_at(Timestamp resolved_at) { this.resolved_at = resolved_at; }

    public int getTechnicien_id() { return technicien_id; }
    public void setTechnicien_id(int technicien_id) { this.technicien_id = technicien_id; }
}
