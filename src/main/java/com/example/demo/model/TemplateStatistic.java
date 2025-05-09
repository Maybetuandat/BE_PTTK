package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class TemplateStatistic extends FraudTemplate {
    
    private Integer totalTemplatesCount;
    private Integer labeledTemplatesCount;
    private Integer unlabeledTemplatesCount;
    private List<TemplateCount> templateCounts;

    public static TemplateStatisticBuilder builder() {
        return new TemplateStatisticBuilder(); 
    }
    public static class TemplateStatisticBuilder {
        private Integer totalTemplatesCount;
        private Integer labeledTemplatesCount;
        private Integer unlabeledTemplatesCount;
        private List<TemplateCount> templateCounts = new ArrayList<>();
        
        public TemplateStatisticBuilder totalTemplatesCount(Integer totalTemplatesCount) {
            this.totalTemplatesCount = totalTemplatesCount;
            return this;
        }
        
        public TemplateStatisticBuilder labeledTemplatesCount(Integer labeledTemplatesCount) {
            this.labeledTemplatesCount = labeledTemplatesCount;
            return this;
        }
        
        public TemplateStatisticBuilder unlabeledTemplatesCount(Integer unlabeledTemplatesCount) {
            this.unlabeledTemplatesCount = unlabeledTemplatesCount;
            return this;
        }
        
        public TemplateStatisticBuilder templateCounts(List<TemplateCount> templateCounts) {
            this.templateCounts = templateCounts;
            return this;
        }
        
        public TemplateStatisticBuilder addTemplateCount(TemplateCount templateCount) {
            this.templateCounts.add(templateCount);
            return this;
        }
        
        public TemplateStatistic build() {

            TemplateStatistic templateStatistic = new TemplateStatistic();
            templateStatistic.setTotalTemplatesCount(this.totalTemplatesCount);
            templateStatistic.setLabeledTemplatesCount(this.labeledTemplatesCount);
            templateStatistic.setUnlabeledTemplatesCount(this.unlabeledTemplatesCount);
            templateStatistic.setTemplateCounts(this.templateCounts);
            return templateStatistic;
        }
    }
}