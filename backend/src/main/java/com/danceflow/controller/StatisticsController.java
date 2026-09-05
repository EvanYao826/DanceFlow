package com.danceflow.controller;

import com.danceflow.common.Result;
import com.danceflow.service.StatisticsService;
import com.danceflow.vo.StatisticsOverviewVO;
import com.danceflow.vo.StatisticsRankingVO;
import com.danceflow.vo.StatisticsTrendVO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController @RequestMapping("/api/admin/statistics")
public class StatisticsController {
    private final StatisticsService service;
    public StatisticsController(StatisticsService service) { this.service = service; }
    @GetMapping("/overview") public Result<StatisticsOverviewVO> overview() { return Result.ok(service.overview()); }
    @GetMapping("/user-trend") public Result<List<StatisticsTrendVO>> userTrend(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) { return Result.ok(service.userTrend(startDate, endDate)); }
    @GetMapping("/activity") public Result<Map<String, Object>> activity(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) { return Result.ok(service.activity(startDate, endDate)); }
    @GetMapping("/content") public Result<Map<String, Long>> content() { return Result.ok(service.content()); }
    @GetMapping("/ranking") public Result<Map<String, List<StatisticsRankingVO>>> ranking() { return Result.ok(service.ranking()); }
}
