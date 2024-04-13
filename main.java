import java.io.File;
import java.io.FileNotFoundException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    public static void main(String[] var0) throws Exception {
        System.out.println("Wrapper started - trying to start NeoForge...");
        Process var1 = (new ProcessBuilder(getCommand(var0))).inheritIO().start();
        int var2 = var1.waitFor();
        System.out.println("Wrapper exited with code " + var2);
        System.exit(var2);
    }

    private static List<String> getCommand(String[] var0) {
        ArrayList var1 = new ArrayList();
        Optional var2 = ProcessHandle.current().info().command();
        if (var2.isEmpty()) {
            System.out.println("Unable to find java path, exiting...");
            System.exit(1);
        }

        var1.add((String)var2.get());
        var1.addAll(ManagementFactory.getRuntimeMXBean().getInputArguments());
        var1.addAll(getForgeArgs());
        var1.addAll(Arrays.asList(var0));
        var1.add("-nogui");
        return var1;
    }

    private static List<String> getForgeArgs() {
        ArrayList var0 = new ArrayList();
        File var1 = new File("libraries/net/neoforged/neoforge/");
        if (!var1.exists()) {
            System.out.println("Unable to find NeoForge, exiting...");
            System.exit(1);
        }

        File[] var2 = var1.listFiles();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            File var5 = var2[var4];
            if (var5.isDirectory()) {
                File var6 = new File("libraries/net/neoforged/neoforge/" + var5.getName() + "/unix_args.txt");

                try {
                    Scanner var7 = new Scanner(var6);

                    while(var7.hasNextLine()) {
                        var0.addAll(List.of(var7.nextLine().split(" ")));
                    }

                    var7.close();
                } catch (FileNotFoundException var8) {
                    System.out.println("Unable to find required NeoForge arguments, exiting...");
                    System.exit(1);
                }

                return var0;
            }
        }

        System.out.println("Unable to find critical NeoForge files, exiting...");
        System.exit(1);
        return var0;
    }
}
