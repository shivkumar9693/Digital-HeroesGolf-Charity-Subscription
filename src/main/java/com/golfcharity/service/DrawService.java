package com.golfcharity.service;

import com.golfcharity.entity.Draw;
import com.golfcharity.entity.DrawResult;
import com.golfcharity.entity.DrawStatus;
import com.golfcharity.repository.DrawRepository;
import com.golfcharity.repository.DrawResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DrawService {

    private final DrawRepository drawRepository;
    private final DrawResultRepository drawResultRepository;

    public List<Draw> getPublishedDraws() {
        return drawRepository.findByStatusOrderByPublishedAtDesc(DrawStatus.PUBLISHED);
    }

    public List<DrawResult> getDrawResults(Draw draw) {
        return drawResultRepository.findByDraw(draw);
    }
}
