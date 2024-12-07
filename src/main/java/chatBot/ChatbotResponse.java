package chatBot;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ChatbotResponse {
    private String model;
    private String createdAt;
    private String response;
    private boolean done;
    private String doneReason;
    private List<Integer> context;
    private long totalDuration;
    private long loadDuration;
    private int promptEvalCount;
    private long promptEvalDuration;
    private int evalCount;
    private long evalDuration;

    // Getters and setters

    @JsonProperty("model")
    public String getModel() {
        return model;
    }

    @JsonProperty("model")
    public void setModel(String model) {
        this.model = model;
    }

    @JsonProperty("created_at")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("created_at")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("response")
    public String getResponse() {
        return response;
    }

    @JsonProperty("response")
    public void setResponse(String response) {
        this.response = response;
    }

    @JsonProperty("done")
    public boolean isDone() {
        return done;
    }

    @JsonProperty("done")
    public void setDone(boolean done) {
        this.done = done;
    }

    @JsonProperty("done_reason")
    public String getDoneReason() {
        return doneReason;
    }

    @JsonProperty("done_reason")
    public void setDoneReason(String doneReason) {
        this.doneReason = doneReason;
    }

    @JsonProperty("context")
    public List<Integer> getContext() {
        return context;
    }

    @JsonProperty("context")
    public void setContext(List<Integer> context) {
        this.context = context;
    }

    @JsonProperty("total_duration")
    public long getTotalDuration() {
        return totalDuration;
    }

    @JsonProperty("total_duration")
    public void setTotalDuration(long totalDuration) {
        this.totalDuration = totalDuration;
    }

    @JsonProperty("load_duration")
    public long getLoadDuration() {
        return loadDuration;
    }

    @JsonProperty("load_duration")
    public void setLoadDuration(long loadDuration) {
        this.loadDuration = loadDuration;
    }

    @JsonProperty("prompt_eval_count")
    public int getPromptEvalCount() {
        return promptEvalCount;
    }

    @JsonProperty("prompt_eval_count")
    public void setPromptEvalCount(int promptEvalCount) {
        this.promptEvalCount = promptEvalCount;
    }

    @JsonProperty("prompt_eval_duration")
    public long getPromptEvalDuration() {
        return promptEvalDuration;
    }

    @JsonProperty("prompt_eval_duration")
    public void setPromptEvalDuration(long promptEvalDuration) {
        this.promptEvalDuration = promptEvalDuration;
    }

    @JsonProperty("eval_count")
    public int getEvalCount() {
        return evalCount;
    }

    @JsonProperty("eval_count")
    public void setEvalCount(int evalCount) {
        this.evalCount = evalCount;
    }

    @JsonProperty("eval_duration")
    public long getEvalDuration() {
        return evalDuration;
    }

    @JsonProperty("eval_duration")
    public void setEvalDuration(long evalDuration) {
        this.evalDuration = evalDuration;
    }
}