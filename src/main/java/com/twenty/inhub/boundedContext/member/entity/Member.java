package com.twenty.inhub.boundedContext.member.entity;

import com.twenty.inhub.base.appConfig.AppConfig;
import com.twenty.inhub.boundedContext.answer.entity.Answer;
import com.twenty.inhub.boundedContext.book.entity.Book;
import com.twenty.inhub.boundedContext.comment.entity.Comment;
import com.twenty.inhub.boundedContext.device.Device;
import com.twenty.inhub.boundedContext.note.entity.Note;
import com.twenty.inhub.boundedContext.post.entity.Post;
import com.twenty.inhub.boundedContext.question.entity.Question;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    private MemberRole role;
    @Enumerated(EnumType.STRING)
    @Setter
    private MemberStatus status;
    private String providerTypeCode;

    private String username;
    private String password;
    @Setter
    private String nickname;
    @Setter
    private String email;
    @Setter
    private String profileImg;
    @Setter
    private int point;

    private String token;

    @CreatedDate
    private LocalDateTime createDate;
    @LastModifiedDate
    private LocalDateTime modifyDate;

    @OneToMany
    @Builder.Default
    private List<Question> questions = new ArrayList<>();
    @OneToMany
    @Builder.Default
    private List<Answer> answers = new ArrayList<>();
    @OneToMany
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();
    @OneToMany
    @Builder.Default
    private List<Post> posts = new ArrayList<>();
    @OneToMany
    @Builder.Default
    private List<Book> books = new ArrayList<>();
    @OneToMany(mappedBy = "receiver")
    @Builder.Default
    private List<Note> receiveList = new ArrayList<>();
    @OneToMany(mappedBy = "sender")
    @Builder.Default
    private List<Note> sendList = new ArrayList<>();
    @OneToMany
    @Builder.Default
    private List<Device> devices = new ArrayList<>();

    //-- create authorize --//
    public List<? extends GrantedAuthority> getGrantedAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        grantedAuthorities.add(new SimpleGrantedAuthority("member"));

        if (isAdmin()) {
            grantedAuthorities.add(new SimpleGrantedAuthority("admin"));
        }

        return grantedAuthorities;
    }

    public void updateRole(MemberRole role) {
        if(this.role != MemberRole.ADMIN && answers.size() >= AppConfig.getMinSizeForSenior()) {
            this.role = role;
        }
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return role.toString().equals("ADMIN");
    }

    public boolean hasSocialProfile() {
        return profileImg.contains("http");
    }

    public int getNewReceiveCount() {
        return (int) receiveList.stream()
                .filter(note -> note.getReadDate() == null)
                .count();
    }

    public int getAllReceiveCount() {
        return receiveList.size();
    }

    public boolean checkDevices(String userAgent) {
        return devices.stream()
                .anyMatch(device -> device.getInfo().equals(userAgent));
    }

    public List<Post> getThreePostNewer() {
        return posts.stream()
                .sorted(Comparator.comparing(Post::getCreatedTime).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "{no : %d, username : %s, type_code : %s} ".formatted(id, username, providerTypeCode);
    }
}
