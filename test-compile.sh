#!/bin/bash

# Roguelike Game Test Compilation Script
# Compiles all test files with proper dependencies

echo "🧪 Compiling Roguelike Game Tests..."

# Configuration
SRC_DIR="src"
TEST_DIR="test/java"
BIN_DIR="bin"
TEST_BIN_DIR="test-bin"
LIB_DIR="lib"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Create directories if they don't exist
echo -e "${BLUE}📁 Creating directories...${NC}"
mkdir -p "$BIN_DIR"
mkdir -p "$TEST_BIN_DIR"

# Check if lib directory exists
if [ ! -d "$LIB_DIR" ]; then
    echo -e "${RED}❌ Error: lib directory not found. Please ensure JUnit and Mockito JARs are in the lib/ directory.${NC}"
    exit 1
fi

# Check for required JAR files
REQUIRED_JARS=(
    "junit-jupiter-api-5.10.1.jar"
    "junit-jupiter-engine-5.10.1.jar"
    "junit-platform-launcher-1.10.1.jar"
    "mockito-core-5.8.0.jar"
)

echo -e "${BLUE}🔍 Checking for required dependencies...${NC}"
MISSING_JARS=()
for jar in "${REQUIRED_JARS[@]}"; do
    if [ ! -f "$LIB_DIR/$jar" ]; then
        MISSING_JARS+=("$jar")
    fi
done

if [ ${#MISSING_JARS[@]} -ne 0 ]; then
    echo -e "${RED}❌ Missing required JAR files:${NC}"
    for jar in "${MISSING_JARS[@]}"; do
        echo -e "${RED}   - $jar${NC}"
    done
    echo -e "${YELLOW}💡 Please download these files to the lib/ directory${NC}"
    exit 1
fi

# Build classpath
CLASSPATH="$BIN_DIR"
for jar in "$LIB_DIR"/*.jar; do
    if [ -f "$jar" ]; then
        CLASSPATH="$CLASSPATH:$jar"
    fi
done

echo -e "${BLUE}🔨 Compiling main source files...${NC}"
# Compile main source files first (if not already compiled)
if [ -d "$SRC_DIR" ]; then
    find "$SRC_DIR" -name "*.java" -print0 | xargs -0 javac -d "$BIN_DIR" -cp "$CLASSPATH" 2>/dev/null
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ Main source compilation successful${NC}"
    else
        echo -e "${YELLOW}⚠️  Main source compilation had issues (may be already compiled)${NC}"
    fi
else
    echo -e "${YELLOW}⚠️  Source directory not found, skipping main compilation${NC}"
fi

# Add test directory and compiled classes to classpath
TEST_CLASSPATH="$TEST_BIN_DIR:$CLASSPATH"

echo -e "${BLUE}🧪 Compiling test files...${NC}"
# Find and compile all test files
if [ -d "$TEST_DIR" ]; then
    TEST_FILES=$(find "$TEST_DIR" -name "*.java")
    
    if [ -n "$TEST_FILES" ]; then
        echo "$TEST_FILES" | xargs javac -d "$TEST_BIN_DIR" -cp "$TEST_CLASSPATH"
        
        if [ $? -eq 0 ]; then
            echo -e "${GREEN}✅ Test compilation successful!${NC}"
            
            # Count compiled test files
            TEST_COUNT=$(find "$TEST_BIN_DIR" -name "*Test.class" | wc -l)
            echo -e "${GREEN}📊 Compiled $TEST_COUNT test classes${NC}"
            
            # Show test structure
            echo -e "${BLUE}📋 Test structure:${NC}"
            find "$TEST_BIN_DIR" -name "*Test.class" | sed 's|test-bin/||' | sed 's|\.class||' | sed 's|/|.|g' | sort
            
        else
            echo -e "${RED}❌ Test compilation failed!${NC}"
            echo -e "${YELLOW}💡 Check for syntax errors or missing dependencies${NC}"
            exit 1
        fi
    else
        echo -e "${YELLOW}⚠️  No test files found in $TEST_DIR${NC}"
    fi
else
    echo -e "${RED}❌ Test directory not found: $TEST_DIR${NC}"
    exit 1
fi

echo -e "${GREEN}🎉 Test compilation completed successfully!${NC}"
echo -e "${BLUE}💡 Run tests with: ./test-run.sh${NC}"