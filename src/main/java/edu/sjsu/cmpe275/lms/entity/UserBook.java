/**
 *
 */
package edu.sjsu.cmpe275.lms.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Entity
@Table(name = "USER_BOOK")
public class UserBook {

    @EmbeddedId
    @Column(name = "ID")
    private UserBookId id;
    @ManyToOne
    @JoinColumn(name = "BOOK", insertable = false, updatable = false)
    private Book book;
    @ManyToOne
    @JoinColumn(name = "USER", insertable = false, updatable = false)
    private User user;
    @Column(name = "CHECKOUT_DATE")
    private String checkout_date;
    @Column(name = "RENEW_FLAG")
    private Integer renew_flag;

    public UserBook() {

    }

    public UserBook(Book b, User u, LocalDate f, Integer renewFlag) {
        // create primary key
        this.id = new UserBookId(b.getBookId(), u.getId());

        // initialize attributes
        this.book = b;
        this.user = u;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        this.checkout_date = dtf.format(LocalDate.now());
        this.renew_flag = renewFlag;
    }

    public void UserBookPersist(Book b, User u) {
        u.getCurrentBooks().add(this);
        b.getCurrentUsers().add(this);

    }

    public String getDueDate() throws ParseException {

        DateFormat dtf = new SimpleDateFormat("yyyy/MM/dd");
        Date duedate = dtf.parse(this.checkout_date);

        Calendar cal = new GregorianCalendar();
        cal.setTime(duedate);
        cal.add(Calendar.DATE, 30);

        String dueDate = dtf.format(cal.getTime());
        System.out.println("String new due date " + dueDate);

        return dueDate;
    }

    @Embeddable
    public static class UserBookId implements Serializable {

        @Column(name = "book")
        protected Integer bookId;

        @Column(name = "user")
        protected Integer userId;

        public UserBookId() {

        }

        public UserBookId(Integer bookId, Integer userId) {
            this.bookId = bookId;
            this.userId = userId;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result
                    + ((bookId == null) ? 0 : bookId.hashCode());
            result = prime * result
                    + ((userId == null) ? 0 : userId.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;

            UserBookId other = (UserBookId) obj;

            if (bookId == null) {
                if (other.bookId != null)
                    return false;
            } else if (!bookId.equals(other.bookId))
                return false;

            if (userId == null) {
                if (other.userId != null)
                    return false;
            } else if (!userId.equals(other.userId))
                return false;

            return true;
        }

    }

}
