package org.example.demo01.service;

import org.example.demo01.common.Result;
import java.util.Map;

public interface DashboardService {

    Result<Map<String, Object>> getStatistics();

    Integer getPendingTasksCount();

    Integer getThisWeekInterviewsCount();

    Map<String, Long> getCandidateStatusCount();

    Map<String, Long> getPositionApplicationCount();

    Map<String, Object> getMonthlyTrend();

    Result<Map<String, Object>> getPositionDemand();

    Result<Map<String, Object>> getChannelDistribution();

    Result<Map<String, Object>> getRecruitmentCycle();

    Result<Map<String, Object>> getPipelineStats();

    Result<Map<String, Object>> getAlerts();

    Result<Map<String, Object>> getCycleStats();

    Result<Map<String, Object>> getPositionStats();

    Result<Map<String, Object>> getTrackingData();
}
