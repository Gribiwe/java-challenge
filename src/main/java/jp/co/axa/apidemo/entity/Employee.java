package jp.co.axa.apidemo.entity;

import lombok.*;

import javax.persistence.*;

/**
 * Object representation for the Employee entity.
 */
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "EMPLOYEE")
public class Employee {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(name = "EMPLOYEE_NAME")
    private String name;

    @Getter
    @Setter
    @Column(name = "EMPLOYEE_SALARY")
    private Integer salary;

    @Getter
    @Setter
    @Column(name = "DEPARTMENT")
    private String department;

}
