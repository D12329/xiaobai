package org.example.demo01.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.demo01.common.Result;
import org.example.demo01.entity.Position;
import org.example.demo01.entity.Resume;
import org.example.demo01.mapper.PositionMapper;
import org.example.demo01.mapper.ResumeMapper;
import org.example.demo01.service.ResumeMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ResumeMatchServiceImpl implements ResumeMatchService {

    @Autowired
    private ResumeMapper resumeMapper;

    @Autowired
    private PositionMapper positionMapper;

    private static final int EDUCATION_WEIGHT = 20;
    private static final int EXPERIENCE_WEIGHT = 30;
    private static final int SKILLS_WEIGHT = 30;
    private static final int PROJECT_WEIGHT = 20;

    private static final Map<String, Integer> EDUCATION_LEVELS = new LinkedHashMap<>();
    static {
        EDUCATION_LEVELS.put("doctor", 5);
        EDUCATION_LEVELS.put("master", 4);
        EDUCATION_LEVELS.put("bachelor", 3);
        EDUCATION_LEVELS.put("diploma", 2);
        EDUCATION_LEVELS.put("high", 1);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> calculateMatch(Long resumeId, Long positionId) {
        Resume resume = resumeMapper.selectById(resumeId);
        Position position = positionMapper.selectById(positionId);

        if (resume == null || position == null) {
            return null;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("resumeId", resumeId);
        result.put("positionId", positionId);

        int educationScore = calculateEducationScore(resume.getEducation(), position.getEducation(), position.getRequirements());
        int experienceScore = calculateExperienceScore(resume.getExperience(), position.getExperience(), position.getRequirements());
        int skillsScore = calculateSkillsScore(resume.getSkills(), position.getTags(), position.getRequirements());
        int projectScore = calculateProjectScore(resume.getSkills(), position.getRequirements());

        int totalScore = educationScore + experienceScore + skillsScore + projectScore;

        result.put("educationScore", educationScore);
        result.put("educationMax", EDUCATION_WEIGHT);
        result.put("experienceScore", experienceScore);
        result.put("experienceMax", EXPERIENCE_WEIGHT);
        result.put("skillsScore", skillsScore);
        result.put("skillsMax", SKILLS_WEIGHT);
        result.put("projectScore", projectScore);
        result.put("projectMax", PROJECT_WEIGHT);
        result.put("totalScore", totalScore);
        result.put("maxScore", 100);

        String matchLevel;
        if (totalScore >= 80) {
            matchLevel = "excellent";
        } else if (totalScore >= 60) {
            matchLevel = "good";
        } else if (totalScore >= 40) {
            matchLevel = "average";
        } else {
            matchLevel = "poor";
        }
        result.put("matchLevel", matchLevel);

        return result;
    }

    private int calculateEducationScore(String education, String positionEducation, String requirements) {
        if (education == null || education.isEmpty()) {
            return 0;
        }

        String edu = education.toLowerCase();
        int educationLevel = 0;
        for (Map.Entry<String, Integer> entry : EDUCATION_LEVELS.entrySet()) {
            if (edu.contains(entry.getKey())) {
                educationLevel = entry.getValue();
                break;
            }
        }

        int requiredLevel = 0;
        if (positionEducation != null && !positionEducation.isEmpty()) {
            String posEdu = positionEducation.toLowerCase();
            for (Map.Entry<String, Integer> entry : EDUCATION_LEVELS.entrySet()) {
                if (posEdu.contains(entry.getKey())) {
                    requiredLevel = entry.getValue();
                    break;
                }
            }
        }

        if (requiredLevel == 0 && requirements != null) {
            String req = requirements.toLowerCase();
            for (Map.Entry<String, Integer> entry : EDUCATION_LEVELS.entrySet()) {
                if (req.contains(entry.getKey())) {
                    requiredLevel = entry.getValue();
                    break;
                }
            }
        }

        if (requiredLevel == 0) {
            return Math.min(educationLevel * 4, EDUCATION_WEIGHT);
        }

        if (educationLevel >= requiredLevel) {
            return EDUCATION_WEIGHT;
        }

        return (educationLevel * EDUCATION_WEIGHT) / requiredLevel;
    }

    private int calculateExperienceScore(Integer experience, Integer positionExperience, String requirements) {
        if (experience == null || experience <= 0) {
            return 0;
        }

        int experienceYears = experience;
        int requiredYears = positionExperience != null ? positionExperience : 0;

        if (requiredYears == 0 && requirements != null) {
            requiredYears = extractYears(requirements);
        }

        if (requiredYears == 0) {
            return Math.min(experienceYears * 5, EXPERIENCE_WEIGHT);
        }

        if (experienceYears >= requiredYears) {
            return EXPERIENCE_WEIGHT;
        }

        return (experienceYears * EXPERIENCE_WEIGHT) / requiredYears;
    }

    private int calculateSkillsScore(String skills, String positionTags, String requirements) {
        if (skills == null || skills.isEmpty()) {
            return 0;
        }

        StringBuilder allRequirements = new StringBuilder();
        if (positionTags != null) {
            allRequirements.append(positionTags).append(" ");
        }
        if (requirements != null) {
            allRequirements.append(requirements);
        }

        String reqText = allRequirements.toString();
        if (reqText.isEmpty()) {
            return SKILLS_WEIGHT / 2;
        }

        Set<String> requiredSkills = extractKeywords(reqText);
        if (requiredSkills.isEmpty()) {
            return SKILLS_WEIGHT / 2;
        }

        Set<String> resumeSkills = extractKeywords(skills);

        int matchCount = 0;
        for (String skill : requiredSkills) {
            if (resumeSkills.contains(skill)) {
                matchCount++;
            }
        }

        double matchRate = (double) matchCount / requiredSkills.size();
        return (int) (matchRate * SKILLS_WEIGHT);
    }

    private int calculateProjectScore(String skills, String requirements) {
        if (skills == null || skills.isEmpty()) {
            return PROJECT_WEIGHT / 2;
        }

        if (requirements == null || requirements.isEmpty()) {
            return PROJECT_WEIGHT / 2;
        }

        Set<String> requiredKeywords = extractKeywords(requirements);
        if (requiredKeywords.isEmpty()) {
            return PROJECT_WEIGHT / 2;
        }

        Set<String> skillKeywords = extractKeywords(skills);

        int matchCount = 0;
        for (String keyword : requiredKeywords) {
            if (skillKeywords.contains(keyword)) {
                matchCount++;
            }
        }

        double matchRate = (double) matchCount / requiredKeywords.size();
        return (int) (matchRate * PROJECT_WEIGHT);
    }

    private int extractYears(String text) {
        if (text == null) {
            return 0;
        }
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isDigit(c)) {
                sb.append(c);
            }
        }
        if (sb.length() > 0) {
            try {
                return Integer.parseInt(sb.toString());
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    private Set<String> extractKeywords(String text) {
        Set<String> keywords = new HashSet<>();
        if (text == null) {
            return keywords;
        }

        String[] commonKeywords = {
            "Java", "Python", "Go", "C++", "C#", "JavaScript", "TypeScript", "PHP", "Ruby", "Swift",
            "Spring", "MyBatis", "Hibernate", "Django", "Flask", "Node.js", "Express", "Vue", "React", "Angular",
            "MySQL", "Oracle", "PostgreSQL", "MongoDB", "Redis", "Elasticsearch", "Kafka", "RabbitMQ",
            "Docker", "Kubernetes", "Jenkins", "Git", "Linux", "AWS", "Azure", "GCP",
            "backend", "distributed", "cache", "message", "data", "frontend", "fullstack",
            "AI", "machine", "deep", "bigdata", "data mining", "NLP",
            "project", "team", "architecture", "performance", "security"
        };

        for (String keyword : commonKeywords) {
            if (text.contains(keyword)) {
                keywords.add(keyword);
            }
        }

        return keywords;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> matchResumesForPosition(Long positionId, Integer limit) {
        Position position = positionMapper.selectById(positionId);
        if (position == null) {
            return Collections.emptyList();
        }

        List<Resume> resumes = resumeMapper.selectList(null);
        List<Map<String, Object>> matchResults = new ArrayList<>();

        for (Resume resume : resumes) {
            Map<String, Object> match = calculateMatch(resume.getId(), positionId);
            if (match != null) {
                match.put("resume", resume);
                matchResults.add(match);
            }
        }

        matchResults.sort((a, b) -> {
            Integer scoreA = (Integer) a.get("totalScore");
            Integer scoreB = (Integer) b.get("totalScore");
            return scoreB.compareTo(scoreA);
        });

        if (limit != null && limit > 0 && matchResults.size() > limit) {
            return matchResults.subList(0, limit);
        }

        return matchResults;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> matchPositionsForResume(Long resumeId, Integer limit) {
        Resume resume = resumeMapper.selectById(resumeId);
        if (resume == null) {
            return Collections.emptyList();
        }

        List<Position> positions = positionMapper.selectList(null);
        List<Map<String, Object>> matchResults = new ArrayList<>();

        for (Position position : positions) {
            Map<String, Object> match = calculateMatch(resumeId, position.getId());
            if (match != null) {
                match.put("position", position);
                matchResults.add(match);
            }
        }

        matchResults.sort((a, b) -> {
            Integer scoreA = (Integer) a.get("totalScore");
            Integer scoreB = (Integer) b.get("totalScore");
            return scoreB.compareTo(scoreA);
        });

        if (limit != null && limit > 0 && matchResults.size() > limit) {
            return matchResults.subList(0, limit);
        }

        return matchResults;
    }

    @Override
    @Transactional(readOnly = true)
    public Result<Map<String, Object>> parseResume(Long resumeId) {
        Resume resume = resumeMapper.selectById(resumeId);
        if (resume == null) {
            return Result.errorWithMsg("Resume not found", 404);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("name", resume.getCandidateName());
        result.put("education", resume.getEducation());
        result.put("workExperience", resume.getWorkExperience());
        result.put("skills", resume.getSkills());
        result.put("projects", resume.getProjects());

        return Result.success(result);
    }

    @Override
    @Transactional(readOnly = true)
    public Result<List<Map<String, Object>>> matchPosition(Long positionId, List<Long> resumeIds, Integer threshold) {
        Position position = positionMapper.selectById(positionId);
        if (position == null) {
            return Result.errorWithMsg("Position not found", 404);
        }

        List<Map<String, Object>> results = new ArrayList<>();
        for (Long resumeId : resumeIds) {
            Resume resume = resumeMapper.selectById(resumeId);
            if (resume == null) {
                continue;
            }
            Map<String, Object> match = calculateMatch(resumeId, positionId);
            if (match != null) {
                Integer totalScore = (Integer) match.get("totalScore");
                if (threshold == null || totalScore >= threshold) {
                    match.put("matchScore", totalScore);
                    match.put("candidateName", resume.getName());
                    match.put("education", resume.getEducation());
                    match.put("experience", resume.getExperience());
                    match.put("skills", resume.getSkills());
                    match.put("positionName", position.getName());
                    
                    String analysis;
                    if (totalScore >= 80) {
                        analysis = "该候选人与职位高度匹配，建议优先安排面试。";
                    } else if (totalScore >= 60) {
                        analysis = "该候选人基本符合职位要求，可安排面试进一步评估。";
                    } else if (totalScore >= 40) {
                        analysis = "该候选人部分条件匹配，建议综合评估后决定是否面试。";
                    } else {
                        analysis = "该候选人与职位匹配度较低，建议考虑其他候选人。";
                    }
                    match.put("analysis", analysis);
                    
                    results.add(match);
                }
            }
        }

        results.sort((a, b) -> {
            Integer scoreA = (Integer) a.get("totalScore");
            Integer scoreB = (Integer) b.get("totalScore");
            return scoreB.compareTo(scoreA);
        });

        return Result.success(results);
    }

    @Override
    @Transactional(readOnly = true)
    public Result<Map<String, Object>> getMatchResult(Long candidateId) {
        Map<String, Object> result = new HashMap<>();
        result.put("candidateId", candidateId);
        result.put("matchLevel", "good");
        result.put("totalScore", 75);
        result.put("recommendations", new ArrayList<>());

        return Result.success(result);
    }
}
