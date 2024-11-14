package com.ibm.example.share_demo;

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

    @GetMapping(value = "/info")
    public String info(@RequestParam(defaultValue = "Share") String name) {
        return "Hello " + name;
    }

    @GetMapping(value = "/rexx", produces = MediaType.APPLICATION_JSON_VALUE)
    public RexxOutput rexxCollector() throws IOException, InterruptedException {
        String rexxName = "COLLECTR";
        Process process = new ProcessBuilder()
                .command("address", "tso", rexxName)
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

        // hacky - override output
        // should be removed if the TSO REXX call works
        processOutput = List.of("ID,PREF,13 Nov 2024,15:41:31");

        // Parse output
        String[] data = processOutput.get(0).split(",");
        RexxOutput rexxOutput = new RexxOutput(data[0], data[1], data[2], data[3]);
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
