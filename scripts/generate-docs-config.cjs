#!/usr/bin/env node

const fs = require('fs');
const path = require('path');

// Configuration
const DOCS_DIR = path.join(__dirname, '..', 'public', 'docs');
const OUTPUT_FILE = path.join(__dirname, '..', 'public', 'docs-config.json');

// Function to extract headers from markdown content
function extractHeaders(content) {
    const lines = content.split('\n');
    const headers = [];
    
    for (const line of lines) {
        const trimmed = line.trim();
        if (trimmed.startsWith('#')) {
            const level = trimmed.match(/^#+/)[0].length;
            const title = trimmed.replace(/^#+\s*/, '');
            const id = title.toLowerCase()
                .replace(/[^\w\s-]/g, '')
                .replace(/\s+/g, '-')
                .replace(/-+/g, '-')
                .replace(/^-|-$/g, '');
            
            headers.push({
                level,
                title,
                id
            });
        }
    }
    
    return headers;
}

// Function to convert file name to section info
function getFileInfo(filename) {
    const match = filename.match(/^(\d+)-(.+)\.md$/);
    if (match) {
        const order = parseInt(match[1]);
        const name = match[2];
        const id = name;
        const title = name.split('-')
            .map(word => word.charAt(0).toUpperCase() + word.slice(1))
            .join(' ');
        
        return { order, id, title, filename };
    }
    
    // Handle files without numeric prefix
    const name = filename.replace(/\.md$/, '');
    return {
        order: 999,
        id: name,
        title: name.split('-')
            .map(word => word.charAt(0).toUpperCase() + word.slice(1))
            .join(' '),
        filename
    };
}

// Function to build navigation structure from headers
function buildNavStructure(headers) {
    const nav = [];
    const stack = [{ children: nav, level: 0 }];
    
    for (const header of headers) {
        // Pop from stack until we find the right parent level
        while (stack.length > 1 && stack[stack.length - 1].level >= header.level) {
            stack.pop();
        }
        
        const navItem = {
            id: header.id,
            title: header.title,
            level: header.level,
            children: []
        };
        
        // Add to current parent
        const parent = stack[stack.length - 1];
        parent.children.push(navItem);
        
        // Push to stack for potential children
        if (header.level < 4) { // Only nest up to h3
            stack.push(navItem);
        }
    }
    
    return nav;
}

// Main function
function generateDocsConfig() {
    console.log('🔍 Scanning documentation files...');
    
    if (!fs.existsSync(DOCS_DIR)) {
        console.error(`❌ Documentation directory not found: ${DOCS_DIR}`);
        process.exit(1);
    }
    
    const files = fs.readdirSync(DOCS_DIR)
        .filter(file => file.endsWith('.md'))
        .sort();
    
    if (files.length === 0) {
        console.error('❌ No markdown files found in docs directory');
        process.exit(1);
    }
    
    console.log(`📄 Found ${files.length} markdown files:`);
    files.forEach(file => console.log(`   - ${file}`));
    
    const docsStructure = {
        generated: new Date().toISOString(),
        sections: []
    };
    
    // Process each markdown file
    for (const file of files) {
        const filePath = path.join(DOCS_DIR, file);
        const content = fs.readFileSync(filePath, 'utf-8');
        const fileInfo = getFileInfo(file);
        const headers = extractHeaders(content);
        const navigation = buildNavStructure(headers);
        
        console.log(`\n📋 Processing ${file}:`);
        console.log(`   Title: ${fileInfo.title}`);
        console.log(`   Headers found: ${headers.length}`);
        
        const section = {
            id: fileInfo.id,
            title: fileInfo.title,
            filename: fileInfo.filename,
            order: fileInfo.order,
            headers: headers,
            navigation: navigation
        };
        
        docsStructure.sections.push(section);
    }
    
    // Sort sections by order
    docsStructure.sections.sort((a, b) => a.order - b.order);
    
    // Write config file
    const configJson = JSON.stringify(docsStructure, null, 2);
    fs.writeFileSync(OUTPUT_FILE, configJson);
    
    console.log(`\n✅ Documentation config generated successfully!`);
    console.log(`📁 Output: ${OUTPUT_FILE}`);
    console.log(`📊 Sections: ${docsStructure.sections.length}`);
    
    // Display structure summary
    console.log('\n📖 Documentation Structure:');
    docsStructure.sections.forEach(section => {
        console.log(`   ${section.order}. ${section.title} (${section.headers.length} headers)`);
        section.navigation.forEach(nav => {
            console.log(`      - ${nav.title}`);
            nav.children.forEach(child => {
                console.log(`        - ${child.title}`);
                child.children.forEach(grandchild => {
                    console.log(`          - ${grandchild.title}`);
                });
            });
        });
    });
}

// Run the script
if (require.main === module) {
    generateDocsConfig();
}

module.exports = { generateDocsConfig, extractHeaders, buildNavStructure };