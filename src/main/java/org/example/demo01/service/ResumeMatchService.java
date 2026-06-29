package org.example.demo01.service;

import org.example.demo01.common.Result;
import java.util.List;
import java.util.Map;

public interface ResumeMatchService {

    Map<String, Object> calculateMatch(Long resumeId, Long positionId);

    List<Map<String, Object>> matchResumesForPosition(Long positionId, Integer limit);

    List<Map<String, Object>> matchPositionsForResume(Long resumeId, Integer limit);

    Result<Map<String, Object>> parseResume(Long resumeId);

    Result<List<Map<String, Object>>> matchPosition(Long positionId, List<Long> resumeIds, Integer threshold);

    Result<Map<String, Object>> getMatchResult(Long candidateId);
}
