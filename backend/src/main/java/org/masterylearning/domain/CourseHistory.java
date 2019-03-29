package org.masterylearning.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 */
@Entity
public class CourseHistory {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne (optional = false)
    public User user;

    @ManyToOne (optional = false)
    public Course course;

    @ManyToOne (optional = false)
    public Entry lastEntry;

    public LocalDateTime created;

    public LocalDateTime modified;

    @OneToMany (mappedBy = "courseHistory", cascade = CascadeType.ALL)
    public List<EntryHistory> entryHistoryList = new ArrayList<> ();

    public List<EntryHistory> getEntryHistoryList () {
        return entryHistoryList;
    }
}
