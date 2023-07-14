package com.project.claveadinamica.repository.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "to_do")
public class ToDo {

    @Id
    public int id;
    public String task;

    @Column("author_id")
    public int authorId;

    public ToDo(){

    }

    public ToDo(String task, int authorId){
        this.task = task;
        this.authorId = authorId;
    }

    public ToDo(int id, String task){
        this.id = id;
        this.task = task;
    }

    public static ToDo from(String task, int id){
        return new ToDo(id, task);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    @Override
    public String toString() {
        return "ToDo{" +
                "id=" + id +
                ", task='" + task + '\'' +
                ", authorId=" + authorId +
                '}';
    }
}
