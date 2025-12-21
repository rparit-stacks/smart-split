package com.rps.smartsplit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "expenses")
public class Expense extends  Audit{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User paidBy;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "expense")
    @JsonIgnore
    private List<ExpenseParticipant> expenseParticipants;

    private Instant expenseDate;



    private String title;

    private String description;

    private BigDecimal amount;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(User paidBy) {
        this.paidBy = paidBy;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<ExpenseParticipant> getExpenseParticipants() {
        return expenseParticipants;
    }

    public void setExpenseParticipants(List<ExpenseParticipant> expenseParticipants) {
        this.expenseParticipants = expenseParticipants;
    }

    public Instant getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(Instant expenseDate) {
        this.expenseDate = expenseDate;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
