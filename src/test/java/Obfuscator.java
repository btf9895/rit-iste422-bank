import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

record BankRecords(Collection<Owner> owners, Collection<Account> accounts, Collection<RegisterEntry> registerEntries) { }

public class Obfuscator {
    private static Logger logger = LogManager.getLogger(Obfuscator.class.getName());

    public BankRecords obfuscate(BankRecords rawObjects) {
        // TODO: Obfuscate and return the records!
        // Fill these in with your own!
        BankRecords obfuscatedRecords = new BankRecords(rawObjects.owners(), rawObjects.accounts(), rawObjects.registerEntries());
        return obfuscatedRecords;
    }

    /**
     * Change the integration test suite to point to our obfuscated production
     * data records.
     *
     * To get the original integration test suite files run
     *   "git checkout -- src/test/resources/persister_integ.properties"
     */
    public void updateIntegProperties() throws IOException {
        Properties props = new Properties();
        File propsFile = new File("src/test/resources/persister_integ.properties");
        if (! propsFile.exists() || !propsFile.canWrite()) {
            throw new RuntimeException("Properties file must exist and be writable: " + propsFile);
        }
        try (InputStream propsStream = new FileInputStream(propsFile)) {
            props.load(propsStream);
        }
        props.setProperty("persisted.suffix", "_prod");
        try (OutputStream propsStream = new FileOutputStream(propsFile)) {
            String comment = String.format(
                    "Note: Don't check in changes to this file!!\n" +
                    "#Modified by %s\n" +
                    "#to reset run 'git checkout -- %s'",
                    this.getClass().getName(), propsFile);
            props.store(propsStream, comment);
        }
    }

    public static void main(String[] args) throws Exception {
        logger.info("Loading Production Records");
        Persister.setPersisterPropertiesFile("persister_prod.properties");
        Persister.loadPersistedFileNameAndDir();
        Bank bank = new Bank();
        bank.loadAllRecords();

        logger.info("Obfuscating records");
        Obfuscator obfuscator = new Obfuscator();
        BankRecords obfuscatedRecords = obfuscator.obfuscate(new BankRecords(bank.getAllOwners(), bank.getAllAccounts(), bank.getAllRegisterEntries()));

        logger.info("Saving obfuscated records");
        obfuscator.updateIntegProperties();
        Persister.resetPersistedFileNameAndDir();
        Persister.setPersisterPropertiesFile("persister_integ.properties");
        Persister.loadPersistedFileNameAndDir();
        // FIXME: writeReords is cribbed from Bank.saveALlRecords(), refactor into common method?
        Persister.writeRecordsToCsv(obfuscatedRecords.owners(), "owners");
        Map<Class<? extends Account>, List<Account>> splitAccounts = obfuscatedRecords
                .accounts()
                .stream()
                .collect(Collectors.groupingBy(rec -> rec.getClass()));
        int savingsCount = Persister.writeRecordsToCsv(splitAccounts.get(SavingsAccount.class), "savings");
        int checkingCount = Persister.writeRecordsToCsv(splitAccounts.get(CheckingAccount.class),"checking");
        Persister.writeRecordsToCsv(obfuscatedRecords.registerEntries(), "register");
    }
}
