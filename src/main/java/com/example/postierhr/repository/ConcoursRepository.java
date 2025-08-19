package com.example.postierhr.repository;

import com.example.postierhr.entity.Concours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ConcoursRepository extends JpaRepository<Concours, UUID> {
    
    List<Concours> findAllByOrderByDateCreationDesc();
    
    List<Concours> findByGradeRequisOrderByDateCreationDesc(String gradeRequis);
    
    @Query("SELECT c FROM Concours c WHERE c.gradeRequis IS NULL OR c.gradeRequis = '' OR c.gradeRequis = :grade ORDER BY c.dateCreation DESC")
    List<Concours> findByGradeRequisOrNull(@Param("grade") String grade);
    
    @Query("SELECT c FROM Concours c WHERE :today BETWEEN c.dateDebutCandidature AND c.dateFinCandidature ORDER BY c.dateFinCandidature ASC")
    List<Concours> findConcoursOuverts(@Param("today") LocalDate today);
    
    @Query("SELECT c FROM Concours c WHERE (c.gradeRequis IS NULL OR c.gradeRequis = '' OR c.gradeRequis = :grade) AND :today BETWEEN c.dateDebutCandidature AND c.dateFinCandidature ORDER BY c.dateFinCandidature ASC")
    List<Concours> findConcoursOuvertsParGrade(@Param("grade") String grade, @Param("today") LocalDate today);
}