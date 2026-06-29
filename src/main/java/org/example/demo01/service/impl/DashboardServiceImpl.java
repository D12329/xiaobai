package org.example.demo01.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.demo01.common.Result;
import org.example.demo01.entity.Candidate;
import org.example.demo01.entity.Interview;
import org.example.demo01.entity.Position;
import org.example.demo01.entity.Resume;
import org.example.demo01.mapper.CandidateMapper;
import org.example.demo01.mapper.InterviewMapper;
import org.example.demo01.mapper.PositionMapper;
import org.example.demo01.mapper.ResumeMapper;
import org.example.demo01.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private CandidateMapper candidateMapper;

    @Autowired
    private InterviewMapper interviewMapper;

    @Autowired
    private PositionMapper positionMapper;

    @Autowired
    private ResumeMapper resumeMapper;

    @Override
    @Transactional(readOnly = true)
    public Result<Map<String, Object>> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        Long totalCandidates = candidateMapper.selectCount(null);
        statistics.put("totalCandidates", totalCandidates);

        Long totalPositions = positionMapper.selectCount(null);
        statistics.put("totalPositions", totalPositions);

        Long totalResumes = resumeMapper.selectCount(null);
        statistics.put("totalResumes", totalResumes);

        Long totalInterviews = interviewMapper.selectCount(null);
        statistics.put("totalInterviews", totalInterviews);

        statistics.put("totalOffers", 0L);
        statistics.put("totalEmployees", 0L);

        statistics.put("pendingTasks", getPendingTasksCount());
        statistics.put("thisWeekInterviews", getThisWeekInterviewsCount());
        statistics.put("candidateStatusCount", getCandidateStatusCount());
        
        List<Candidate> candidates = candidateMapper.selectList(null);
        LocalDateTime now = LocalDateTime.now();
        long overdueCount = candidates.stream()
            .filter(c -> c.getCreatedAt() != null && c.getStatus() != null && c.getStatus() <= 3)
            .filter(c -> java.time.temporal.ChronoUnit.DAYS.between(c.getCreatedAt(), now) > 7)
            .count();
        statistics.put("overdueCount", overdueCount);
        
        long totalDays = candidates.stream()
            .filter(c -> c.getCreatedAt() != null && c.getStatus() != null && c.getStatus() >= 4)
            .mapToLong(c -> java.time.temporal.ChronoUnit.DAYS.between(c.getCreatedAt(), now))
            .sum();
        long hiredCount = candidates.stream()
            .filter(c -> c.getStatus() != null && c.getStatus() >= 4)
            .count();
        int averageCycleDays = hiredCount > 0 ? (int) (totalDays / hiredCount) : 0;
        statistics.put("averageCycleDays", averageCycleDays);

        return Result.success(statistics);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getPendingTasksCount() {
        LambdaQueryWrapper<Candidate> candidateWrapper = new LambdaQueryWrapper<>();
        candidateWrapper.eq(Candidate::getStatus, 1);
        Long pendingCandidates = candidateMapper.selectCount(candidateWrapper);

        LambdaQueryWrapper<Interview> interviewWrapper = new LambdaQueryWrapper<>();
        interviewWrapper.eq(Interview::getStatus, "scheduled");
        interviewWrapper.gt(Interview::getStartTime, LocalDateTime.now());
        Long scheduledInterviews = interviewMapper.selectCount(interviewWrapper);

        return pendingCandidates.intValue() + scheduledInterviews.intValue();
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getThisWeekInterviewsCount() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfWeek = now.minusDays(now.getDayOfWeek().getValue() - 1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfWeek = startOfWeek.plusDays(7);

        LambdaQueryWrapper<Interview> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(Interview::getStartTime, startOfWeek);
        wrapper.le(Interview::getStartTime, endOfWeek);
        return interviewMapper.selectCount(wrapper).intValue();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getCandidateStatusCount() {
        Map<String, Long> statusCount = new LinkedHashMap<>();
        List<Candidate> candidates = candidateMapper.selectList(null);

        for (Candidate candidate : candidates) {
            Integer status = candidate.getStatus();
            if (status != null) {
                String statusStr = getStatusString(status);
                statusCount.merge(statusStr, 1L, Long::sum);
            }
        }

        return statusCount;
    }

    private String getStatusString(Integer status) {
        switch (status) {
            case 1: return "pending";
            case 2: return "interviewing";
            case 3: return "offer";
            case 4: return "hired";
            case 5: return "rejected";
            default: return "unknown";
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getPositionApplicationCount() {
        Map<String, Long> positionCount = new LinkedHashMap<>();
        List<Candidate> candidates = candidateMapper.selectList(null);

        for (Candidate candidate : candidates) {
            Long positionId = candidate.getPositionId();
            if (positionId != null) {
                Position position = positionMapper.selectById(positionId);
                String positionName = position != null ? position.getName() : "unknown";
                positionCount.merge(positionName, 1L, Long::sum);
            }
        }

        return positionCount;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getMonthlyTrend() {
        Map<String, Object> trend = new LinkedHashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        LocalDateTime now = LocalDateTime.now();
        for (int i = 5; i >= 0; i--) {
            LocalDateTime monthStart = now.minusMonths(i).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            LocalDateTime monthEnd = monthStart.plusMonths(1);
            String monthKey = monthStart.format(formatter);

            LambdaQueryWrapper<Candidate> candidateWrapper = new LambdaQueryWrapper<>();
            candidateWrapper.ge(Candidate::getCreatedAt, monthStart);
            candidateWrapper.lt(Candidate::getCreatedAt, monthEnd);
            Long candidateCount = candidateMapper.selectCount(candidateWrapper);

            LambdaQueryWrapper<Resume> resumeWrapper = new LambdaQueryWrapper<>();
            resumeWrapper.ge(Resume::getCreatedAt, monthStart);
            resumeWrapper.lt(Resume::getCreatedAt, monthEnd);
            Long resumeCount = resumeMapper.selectCount(resumeWrapper);

            Map<String, Long> monthData = new HashMap<>();
            monthData.put("candidates", candidateCount);
            monthData.put("resumes", resumeCount);
            trend.put(monthKey, monthData);
        }

        return trend;
    }

    @Override
    @Transactional(readOnly = true)
    public Result<Map<String, Object>> getPositionDemand() {
        Map<String, Object> result = new HashMap<>();
        result.put("positionDemand", getPositionApplicationCount());
        return Result.success(result);
    }

    @Override
    @Transactional(readOnly = true)
    public Result<Map<String, Object>> getChannelDistribution() {
        Map<String, Object> result = new HashMap<>();
        Map<String, Long> channelCount = new LinkedHashMap<>();

        List<Resume> resumes = resumeMapper.selectList(null);
        for (Resume resume : resumes) {
            String source = resume.getSource();
            if (source == null || source.isEmpty()) {
                source = "unknown";
            }
            channelCount.merge(source, 1L, Long::sum);
        }

        result.put("channelDistribution", channelCount);
        return Result.success(result);
    }

    @Override
    @Transactional(readOnly = true)
    public Result<Map<String, Object>> getRecruitmentCycle() {
        Map<String, Object> result = new HashMap<>();
        result.put("monthlyTrend", getMonthlyTrend());
        return Result.success(result);
    }

    @Override
    @Transactional(readOnly = true)
    public Result<Map<String, Object>> getPipelineStats() {
        Map<String, Object> result = new HashMap<>();
        
        LambdaQueryWrapper<Candidate> pendingWrapper = new LambdaQueryWrapper<>();
        pendingWrapper.eq(Candidate::getStatus, 1);
        Long pendingCount = candidateMapper.selectCount(pendingWrapper);
        
        LambdaQueryWrapper<Candidate> interviewingWrapper = new LambdaQueryWrapper<>();
        interviewingWrapper.eq(Candidate::getStatus, 2);
        Long interviewingCount = candidateMapper.selectCount(interviewingWrapper);
        
        LambdaQueryWrapper<Candidate> offeredWrapper = new LambdaQueryWrapper<>();
        offeredWrapper.eq(Candidate::getStatus, 3);
        Long offeredCount = candidateMapper.selectCount(offeredWrapper);
        
        LambdaQueryWrapper<Candidate> hiredWrapper = new LambdaQueryWrapper<>();
        hiredWrapper.eq(Candidate::getStatus, 4);
        Long hiredCount = candidateMapper.selectCount(hiredWrapper);
        
        result.put("pending", pendingCount);
        result.put("interviewing", interviewingCount);
        result.put("offered", offeredCount);
        result.put("hired", hiredCount);
        
        List<Candidate> pendingList = candidateMapper.selectList(pendingWrapper);
        List<Candidate> interviewingList = candidateMapper.selectList(interviewingWrapper);
        List<Candidate> offeredList = candidateMapper.selectList(offeredWrapper);
        List<Candidate> hiredList = candidateMapper.selectList(hiredWrapper);
        
        List<Map<String, Object>> pendingData = new java.util.ArrayList<>();
        List<Map<String, Object>> interviewingData = new java.util.ArrayList<>();
        List<Map<String, Object>> offeredData = new java.util.ArrayList<>();
        List<Map<String, Object>> hiredData = new java.util.ArrayList<>();
        
        for (Candidate c : pendingList) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", c.getId());
            item.put("name", c.getName());
            item.put("positionName", c.getPositionId() != null ? positionMapper.selectById(c.getPositionId()) != null ? positionMapper.selectById(c.getPositionId()).getName() : "未知职位" : "未知职位");
            item.put("waitDays", 1);
            pendingData.add(item);
        }
        
        for (Candidate c : interviewingList) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", c.getId());
            item.put("name", c.getName());
            item.put("positionName", c.getPositionId() != null ? positionMapper.selectById(c.getPositionId()) != null ? positionMapper.selectById(c.getPositionId()).getName() : "未知职位" : "未知职位");
            item.put("interviewCount", 1);
            interviewingData.add(item);
        }
        
        for (Candidate c : offeredList) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", c.getId());
            item.put("name", c.getName());
            item.put("positionName", c.getPositionId() != null ? positionMapper.selectById(c.getPositionId()) != null ? positionMapper.selectById(c.getPositionId()).getName() : "未知职位" : "未知职位");
            item.put("waitDays", 1);
            offeredData.add(item);
        }
        
        for (Candidate c : hiredList) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", c.getId());
            item.put("name", c.getName());
            item.put("positionName", c.getPositionId() != null ? positionMapper.selectById(c.getPositionId()) != null ? positionMapper.selectById(c.getPositionId()).getName() : "未知职位" : "未知职位");
            item.put("joinDate", "-");
            hiredData.add(item);
        }
        
        result.put("pendingList", pendingData);
        result.put("interviewingList", interviewingData);
        result.put("offeredList", offeredData);
        result.put("hiredList", hiredData);
        
        return Result.success(result);
    }

    @Override
    @Transactional(readOnly = true)
    public Result<Map<String, Object>> getAlerts() {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> alerts = new java.util.ArrayList<>();
        
        List<Candidate> candidates = candidateMapper.selectList(null);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        for (Candidate candidate : candidates) {
            LocalDateTime createdAt = candidate.getCreatedAt();
            if (createdAt == null) continue;
            
            long days = java.time.temporal.ChronoUnit.DAYS.between(createdAt, now);
            Integer status = candidate.getStatus();
            
            if (days > 7 && status != null && status <= 3) {
                Map<String, Object> alert = new HashMap<>();
                alert.put("id", candidate.getId());
                alert.put("title", "候选人处理超时");
                alert.put("description", candidate.getName() + " 在" + getStatusString(status) + "环节已等待" + days + "天");
                alert.put("priority", days > 14 ? "high" : "medium");
                alert.put("time", createdAt.format(formatter));
                alerts.add(alert);
            }
        }
        
        List<Interview> interviews = interviewMapper.selectList(null);
        for (Interview interview : interviews) {
            LocalDateTime startTime = interview.getStartTime();
            if (startTime != null && startTime.isBefore(now) && "scheduled".equals(interview.getStatus())) {
                Map<String, Object> alert = new HashMap<>();
                alert.put("id", interview.getId());
                alert.put("title", "面试已超时");
                alert.put("description", interview.getInterviewerName() + " 的面试已超过预定时间");
                alert.put("priority", "high");
                alert.put("time", startTime.format(formatter));
                alerts.add(alert);
            }
        }
        
        result.put("alerts", alerts);
        return Result.success(result);
    }

    @Override
    @Transactional(readOnly = true)
    public Result<Map<String, Object>> getCycleStats() {
        Map<String, Object> result = new HashMap<>();
        
        List<Map<String, Object>> cycleData = List.of(
            Map.of("stage", "简历筛选", "avgDays", 3L),
            Map.of("stage", "初试", "avgDays", 5L),
            Map.of("stage", "复试", "avgDays", 4L),
            Map.of("stage", "终面", "avgDays", 3L),
            Map.of("stage", "Offer", "avgDays", 5L)
        );
        
        result.put("cycleData", cycleData);
        return Result.success(result);
    }

    @Override
    @Transactional(readOnly = true)
    public Result<Map<String, Object>> getPositionStats() {
        Map<String, Object> result = new HashMap<>();
        Map<String, Long> positionCount = getPositionApplicationCount();
        List<Map<String, Object>> positions = new java.util.ArrayList<>();
        for (Map.Entry<String, Long> entry : positionCount.entrySet()) {
            Map<String, Object> pos = new HashMap<>();
            pos.put("name", entry.getKey());
            pos.put("count", entry.getValue());
            positions.add(pos);
        }
        result.put("positions", positions);
        return Result.success(result);
    }

    @Override
    @Transactional(readOnly = true)
    public Result<Map<String, Object>> getTrackingData() {
        Map<String, Object> result = new HashMap<>();
        
        long totalPositions = positionMapper.selectCount(null);
        long totalCandidates = candidateMapper.selectCount(null);
        int averageDays = 0;
        
        LambdaQueryWrapper<Candidate> pendingWrapper = new LambdaQueryWrapper<>();
        pendingWrapper.eq(Candidate::getStatus, 1);
        Long pendingCount = candidateMapper.selectCount(pendingWrapper);
        
        LambdaQueryWrapper<Candidate> interviewingWrapper = new LambdaQueryWrapper<>();
        interviewingWrapper.eq(Candidate::getStatus, 2);
        Long interviewingCount = candidateMapper.selectCount(interviewingWrapper);
        
        LambdaQueryWrapper<Candidate> offeredWrapper = new LambdaQueryWrapper<>();
        offeredWrapper.eq(Candidate::getStatus, 3);
        Long offeredCount = candidateMapper.selectCount(offeredWrapper);
        
        LambdaQueryWrapper<Candidate> hiredWrapper = new LambdaQueryWrapper<>();
        hiredWrapper.eq(Candidate::getStatus, 4);
        Long hiredCount = candidateMapper.selectCount(hiredWrapper);
        
        result.put("totalPositions", totalPositions);
        result.put("totalCandidates", totalCandidates);
        result.put("averageCycleDays", averageDays);
        result.put("pending", pendingCount);
        result.put("interviewing", interviewingCount);
        result.put("offered", offeredCount);
        result.put("hired", hiredCount);
        
        List<Candidate> pendingList = candidateMapper.selectList(pendingWrapper);
        List<Candidate> interviewingList = candidateMapper.selectList(interviewingWrapper);
        List<Candidate> offeredList = candidateMapper.selectList(offeredWrapper);
        List<Candidate> hiredList = candidateMapper.selectList(hiredWrapper);
        
        List<Map<String, Object>> pendingData = new java.util.ArrayList<>();
        List<Map<String, Object>> interviewingData = new java.util.ArrayList<>();
        List<Map<String, Object>> offeredData = new java.util.ArrayList<>();
        List<Map<String, Object>> hiredData = new java.util.ArrayList<>();
        
        for (Candidate c : pendingList) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", c.getId());
            item.put("name", c.getName());
            item.put("positionName", c.getPositionId() != null ? positionMapper.selectById(c.getPositionId()) != null ? positionMapper.selectById(c.getPositionId()).getName() : "未知职位" : "未知职位");
            item.put("waitDays", 1);
            pendingData.add(item);
        }
        
        for (Candidate c : interviewingList) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", c.getId());
            item.put("name", c.getName());
            item.put("positionName", c.getPositionId() != null ? positionMapper.selectById(c.getPositionId()) != null ? positionMapper.selectById(c.getPositionId()).getName() : "未知职位" : "未知职位");
            item.put("interviewCount", 1);
            interviewingData.add(item);
        }
        
        for (Candidate c : offeredList) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", c.getId());
            item.put("name", c.getName());
            item.put("positionName", c.getPositionId() != null ? positionMapper.selectById(c.getPositionId()) != null ? positionMapper.selectById(c.getPositionId()).getName() : "未知职位" : "未知职位");
            item.put("waitDays", 1);
            offeredData.add(item);
        }
        
        for (Candidate c : hiredList) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", c.getId());
            item.put("name", c.getName());
            item.put("positionName", c.getPositionId() != null ? positionMapper.selectById(c.getPositionId()) != null ? positionMapper.selectById(c.getPositionId()).getName() : "未知职位" : "未知职位");
            item.put("joinDate", "-");
            hiredData.add(item);
        }
        
        result.put("pendingList", pendingData);
        result.put("interviewingList", interviewingData);
        result.put("offeredList", offeredData);
        result.put("hiredList", hiredData);
        
        return Result.success(result);
    }
}
