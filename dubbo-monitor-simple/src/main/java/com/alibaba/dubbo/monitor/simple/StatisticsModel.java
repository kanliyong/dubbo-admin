package com.alibaba.dubbo.monitor.simple;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 *

 count://192.168.1.63:48886/com.eqxiu.bigdata.common.api.CommonApi/mapQuery?
 application=CommonService&concurrent=0&consumer=172.21.117.187&elapsed=0
 &failure=0&input=0&interface=com.eqxiu.bigdata.common.api.CommonApi&max.concurrent=0
 &max.elapsed=0&max.input=0&max.output=0&method=mapQuery&output=0&success=0&timestamp=1565942592312

 */
public class StatisticsModel {

    public int INTERVAL = 60;
    private Long id;

    private String service;
    private String method;
    private String application;
    private String dubboVersion;

    private String side;
    private String provider;
    private String consumer;

    private String host;
    private int port;

    private Integer elapsed;
    private Integer failure;
    private Integer success;
    private Integer concurrent;
    private Integer input;
    private Integer output;


    private Integer maxConcurrent;
    private Integer maxElapsed;
    private Integer maxInput;
    private Integer maxOutput;

    private Date createAt;

    private URL url;
    private Double qps;
    private Double faillQps;

    public StatisticsModel() {
    }

    public Double getQps() {
        return Double.valueOf(success) / INTERVAL;
    }

    public void setQps(Double qps) {
        this.qps = qps;
    }

    public Double getFaillQps() {
        return Double.valueOf(failure) / INTERVAL;
    }

    public void setFaillQps(Double faillQps) {
        this.faillQps = faillQps;
    }

    @Override
    public String toString() {
        return "StatisticsModel{" +
                "id=" + id +
                ", service='" + service + '\'' +
                ", method='" + method + '\'' +
                ", application='" + application + '\'' +
                ", dubboVersion='" + dubboVersion + '\'' +
                ", side='" + side + '\'' +
                ", provider='" + provider + '\'' +
                ", consumer='" + consumer + '\'' +
                ", elapsed=" + elapsed +
                ", failure=" + failure +
                ", success=" + success +
                ", concurrent=" + concurrent +
                ", input=" + input +
                ", output=" + output +
                ", maxConcurrent=" + maxConcurrent +
                ", maxElapsed=" + maxElapsed +
                ", maxInput=" + maxInput +
                ", maxOutput=" + maxOutput +
                ", createAt=" + createAt +
                ", url=" + url +
                ", qps=" + qps +
                ", faillQps=" + faillQps +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        StatisticsModel model = (StatisticsModel) o;

        return new EqualsBuilder()
                .append(elapsed, model.elapsed)
                .append(failure, model.failure)
                .append(success, model.success)
                .append(concurrent, model.concurrent)
                .append(input, model.input)
                .append(output, model.output)
                .append(maxConcurrent, model.maxConcurrent)
                .append(maxElapsed, model.maxElapsed)
                .append(maxInput, model.maxInput)
                .append(maxOutput, model.maxOutput)
                .append(service, model.service)
                .append(method, model.method)
                .append(application, model.application)
                .append(dubboVersion, model.dubboVersion)
                .append(side, model.side)
                .append(provider, model.provider)
                .append(consumer, model.consumer)
                .append(createAt, model.createAt)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(service)
                .append(method)
                .append(application)
                .append(dubboVersion)
                .append(side)
                .append(provider)
                .append(consumer)
                .append(elapsed)
                .append(failure)
                .append(success)
                .append(concurrent)
                .append(input)
                .append(output)
                .append(maxConcurrent)
                .append(maxElapsed)
                .append(maxInput)
                .append(maxOutput)
                .append(createAt)
                .toHashCode();
    }

    public StatisticsModel(URL statistics) {
        url = statistics;
        service = statistics.getParameter("interface");
        method = statistics.getParameter("method");
        application = statistics.getParameter("application");
        dubboVersion = statistics.getParameter("dubbo");

        host = statistics.getHost();
        port = statistics.getPort();

        provider = statistics.getParameter("provider", "");
        consumer = statistics.getParameter("consumer", "");

        elapsed = statistics.getParameter("elapsed", 0);
        failure = statistics.getParameter("failure", 0);
        success = statistics.getParameter("success", 0);
        concurrent = statistics.getParameter("concurrent", 0);
        input = statistics.getParameter("input", 0);
        output = statistics.getParameter("output", 0);

        maxConcurrent = statistics.getParameter("max.concurrent", 0);
        maxElapsed = statistics.getParameter("max.elapsed", 0);
        maxInput = statistics.getParameter("max.input", 0);
        maxOutput = statistics.getParameter("max.output", 0);

        long timestamp = statistics.getParameter("timestamp", 0L);
        if (timestamp == 0) {
            createAt = new Date();
        } else {
            createAt = new Date(timestamp);
        }

        if (StringUtils.isNotEmpty(provider)) {
            side = Constants.CONSUMER_SIDE;
        } else if (StringUtils.isNotEmpty(consumer)) {
            side = Constants.PROVIDER_SIDE;
        } else {
            side = "UNKOWN";
        }

    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getDubboVersion() {
        return dubboVersion;
    }

    public void setDubboVersion(String dubboVersion) {
        this.dubboVersion = dubboVersion;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public int getElapsed() {
        return elapsed;
    }

    public void setElapsed(int elapsed) {
        this.elapsed = elapsed;
    }

    public int getFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getConcurrent() {
        return concurrent;
    }

    public void setConcurrent(int concurrent) {
        this.concurrent = concurrent;
    }

    public int getInput() {
        return input;
    }

    public void setInput(int input) {
        this.input = input;
    }

    public int getOutput() {
        return output;
    }

    public void setOutput(int output) {
        this.output = output;
    }

    public int getMaxConcurrent() {
        return maxConcurrent;
    }

    public void setMaxConcurrent(int maxConcurrent) {
        this.maxConcurrent = maxConcurrent;
    }

    public int getMaxElapsed() {
        return maxElapsed;
    }

    public void setMaxElapsed(int maxElapsed) {
        this.maxElapsed = maxElapsed;
    }

    public int getMaxInput() {
        return maxInput;
    }

    public void setMaxInput(int maxInput) {
        this.maxInput = maxInput;
    }

    public int getMaxOutput() {
        return maxOutput;
    }

    public void setMaxOutput(int maxOutput) {
        this.maxOutput = maxOutput;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setElapsed(Integer elapsed) {
        this.elapsed = elapsed;
    }

    public void setFailure(Integer failure) {
        this.failure = failure;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public void setConcurrent(Integer concurrent) {
        this.concurrent = concurrent;
    }

    public void setInput(Integer input) {
        this.input = input;
    }

    public void setOutput(Integer output) {
        this.output = output;
    }

    public void setMaxConcurrent(Integer maxConcurrent) {
        this.maxConcurrent = maxConcurrent;
    }

    public void setMaxElapsed(Integer maxElapsed) {
        this.maxElapsed = maxElapsed;
    }

    public void setMaxInput(Integer maxInput) {
        this.maxInput = maxInput;
    }

    public void setMaxOutput(Integer maxOutput) {
        this.maxOutput = maxOutput;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
        Date d = df.parse("14/Aug/2019:15:28:40 +0800");
        System.out.println(d);
    }
}
