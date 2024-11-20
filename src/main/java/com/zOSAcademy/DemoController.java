package com.zOSAcademy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


@RestController
public class DemoController {

    @Value("${REXX_EXEC:NOT_FOUND}") String rexxExec;

    @GetMapping(value = "/info")
    public String info(@RequestParam(defaultValue = "Share") String name) {
        return "Hello " + name;
    }

    @GetMapping(value = "/rexx", produces = MediaType.APPLICATION_JSON_VALUE)
    public RexxOutput rexxCollector() throws IOException, InterruptedException {
        Process process = new ProcessBuilder()
                .command("tsocmd", "ex '" +rexxExec+"'")
                //.command("java", "-version") // use this for local testing
                .redirectErrorStream(true)
                .start();

        // Trap the output, so we can use the data
        List<String> processOutput = this.getProcessOutput(process);
        if (processOutput.isEmpty()) {
            throw new IllegalStateException("No data available.");
        }

        // Print output
        processOutput.forEach(System.out::println);

        // Parse output
        String[] data = processOutput.get(2).split(",");
        String daytime = processOutput.get(4);
        RexxOutput rexxOutput = new RexxOutput(data[0], data[1], data[2], daytime);
        System.out.println(rexxOutput);
        return rexxOutput;
    }

    private List<String> getProcessOutput(Process process) throws IOException, InterruptedException {
        List<String> output = new ArrayList<>();
        try (BufferedReader processOutputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));) {
            String readLine;

            while ((readLine = processOutputReader.readLine()) != null) {
                output.add(readLine);
            }

            process.waitFor();
        }
        return output;
    }
}
