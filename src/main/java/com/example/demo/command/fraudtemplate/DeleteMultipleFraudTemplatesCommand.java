package com.example.demo.command.fraudtemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.demo.command.Command;
import com.example.demo.model.BoundingBox;
import com.example.demo.model.FraudTemplate;
import com.example.demo.service.FileStorageService;
import com.example.demo.service.FraudTemplateService;

public class DeleteMultipleFraudTemplatesCommand implements Command {
    private FraudTemplateService fraudTemplateService;
    private List<Integer> templateIds;
    private List<FraudTemplate> deletedTemplates;
    
    private final String UPLOAD_DIR = "images/";
    private final String BACKUP_DIR = "backup/";
    private final Timer timer = new Timer(true);
    private final List<String> backedUpFiles = new ArrayList<>();

    public DeleteMultipleFraudTemplatesCommand(FraudTemplateService fraudTemplateService, List<Integer> templateIds) {
        this.fraudTemplateService = fraudTemplateService;
        this.templateIds = templateIds;
        this.deletedTemplates = new ArrayList<>();
        
    }

    @Override
    public void execute() {
        if (templateIds != null && !templateIds.isEmpty()) {
            createBackupDirectory();
            for (Integer id : templateIds) {
                if (fraudTemplateService.existsById(id)) {
                    deletedTemplates.add(fraudTemplateService.getFraudTemplateById(id));
                    backupImage(fraudTemplateService.getFraudTemplateById(id).getName());
                }
            }
            fraudTemplateService.deleteFraudTemplates(templateIds);
            scheduleBackupCleanup();
        }
    }

    @Override
    public void undo() {
         if (deletedTemplates != null && !deletedTemplates.isEmpty()) {
            for (FraudTemplate template : deletedTemplates) {
                template.setId(null); 
                restoreImageFromBackup(template.getName());
                if (template.getBoundingBoxes() != null && !template.getBoundingBoxes().isEmpty()) {
                    for (BoundingBox oldBox : template.getBoundingBoxes()) {
                        
                      oldBox.setId(null);    
                    }
                }
                FraudTemplate savedTemplate = fraudTemplateService.addFraudTemplate(template);
                System.out.println(">> Undo: Đã thêm lại template với ID: " + savedTemplate.getId());
            }
        }
    }
    private void createBackupDirectory() {
        Path backupPath = Paths.get(BACKUP_DIR);
        try {
            if (!Files.exists(backupPath)) {
                Files.createDirectories(backupPath);
                System.out.println("Created backup directory: " + backupPath.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Failed to create backup directory: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void restoreImageFromBackup(String fileName) {
        try {
            Path sourcePath = Paths.get(BACKUP_DIR + fileName);
            Path targetPath = Paths.get(UPLOAD_DIR + fileName);
            
            if (Files.exists(sourcePath)) {
                // Ensure target directory exists
                Files.createDirectories(targetPath.getParent());
                
                Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Restored image from backup: " + fileName);
            } else {
                System.out.println("Backup image not found for restore: " + fileName);
            }
        } catch (IOException e) {
            System.err.println("Failed to restore image " + fileName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void cleanupBackupDirectory() {
        System.out.println("Cleaning up backup directory...");
        
        for (String fileName : backedUpFiles) {
            try {
                Path filePath = Paths.get(BACKUP_DIR + fileName);
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                    System.out.println("Deleted backup file: " + fileName);
                }
            } catch (IOException e) {
                System.err.println("Failed to delete backup file " + fileName + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        // After cleanup, clear the list
        backedUpFiles.clear();
    }
    private void scheduleBackupCleanup() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                cleanupBackupDirectory();
            }
        }, 30000); // 30 seconds
    }

     private void backupImage(String fileName) {
        try {
            Path sourcePath = Paths.get(UPLOAD_DIR + fileName);
            Path targetPath = Paths.get(BACKUP_DIR + fileName);
            
            if (Files.exists(sourcePath)) {
                Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                backedUpFiles.add(fileName);
                System.out.println("Backed up image: " + fileName);
            } else {
                System.out.println("Source image not found for backup: " + fileName);
            }
        } catch (IOException e) {
            System.err.println("Failed to backup image " + fileName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}