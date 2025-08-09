#!/bin/bash

# Roguelike Game Test Execution Script
# Runs all JUnit 5 tests with proper reporting

echo "🚀 Running Roguelike Game Tests..."

# Configuration
BIN_DIR="bin"
TEST_BIN_DIR="test-bin"
LIB_DIR="lib"
REPORTS_DIR="test-reports"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
NC='\033[0m' # No Color

# Create reports directory
mkdir -p "$REPORTS_DIR"

# Check if compiled tests exist
if [ ! -d "$TEST_BIN_DIR" ]; then
    echo -e "${RED}❌ Test classes not found. Please run ./test-compile.sh first${NC}"
    exit 1
fi

# Build classpath
CLASSPATH="$TEST_BIN_DIR:$BIN_DIR"
for jar in "$LIB_DIR"/*.jar; do
    if [ -f "$jar" ]; then
        CLASSPATH="$CLASSPATH:$jar"
    fi
done

# Function to run a specific test class
run_test_class() {
    local test_class="$1"
    local class_name=$(basename "$test_class" .class)
    
    echo -e "${BLUE}🧪 Running: $class_name${NC}"
    
    # Run the test and capture output
    java -cp "$CLASSPATH" \
        -Djava.awt.headless=true \
        -Dtestfx.robot=glass \
        -Dtestfx.headless=true \
        -Dprism.order=sw \
        -Dprism.text=t2k \
        -Dglass.platform=Monocle \
        -Dmonocle.platform=Headless \
        -Dprism.verbose=true \
        org.junit.platform.console.ConsoleLauncher \
        --class-path="$CLASSPATH" \
        --select-class="$test_class" \
        --details=tree \
        --reports-dir="$REPORTS_DIR" \
        2>&1 | tee "$REPORTS_DIR/${class_name}_output.txt"
    
    return ${PIPESTATUS[0]}
}

# Function to run all tests
run_all_tests() {
    echo -e "${PURPLE}🎯 Running all tests...${NC}"
    
    java -cp "$CLASSPATH" \
        -Djava.awt.headless=true \
        -Dtestfx.robot=glass \
        -Dtestfx.headless=true \
        -Dprism.order=sw \
        -Dprism.text=t2k \
        -Dglass.platform=Monocle \
        -Dmonocle.platform=Headless \
        -Dprism.verbose=true \
        org.junit.platform.console.ConsoleLauncher \
        --class-path="$CLASSPATH" \
        --scan-class-path="$TEST_BIN_DIR" \
        --details=tree \
        --reports-dir="$REPORTS_DIR" \
        2>&1 | tee "$REPORTS_DIR/all_tests_output.txt"
    
    return ${PIPESTATUS[0]}
}

# Function to run tests by package
run_package_tests() {
    local package="$1"
    echo -e "${BLUE}📦 Running tests in package: $package${NC}"
    
    java -cp "$CLASSPATH" \
        -Djava.awt.headless=true \
        -Dtestfx.robot=glass \
        -Dtestfx.headless=true \
        -Dprism.order=sw \
        -Dprism.text=t2k \
        -Dglass.platform=Monocle \
        -Dmonocle.platform=Headless \
        -Dprism.verbose=true \
        org.junit.platform.console.ConsoleLauncher \
        --class-path="$CLASSPATH" \
        --select-package="$package" \
        --details=tree \
        --reports-dir="$REPORTS_DIR" \
        2>&1 | tee "$REPORTS_DIR/${package}_output.txt"
    
    return ${PIPESTATUS[0]}
}

# Function to display test summary
display_summary() {
    echo -e "\n${PURPLE}📊 Test Summary${NC}"
    echo -e "${BLUE}==================${NC}"
    
    if [ -f "$REPORTS_DIR/all_tests_output.txt" ]; then
        # Extract test results from output
        TESTS_RUN=$(grep -o "Test run finished after" "$REPORTS_DIR/all_tests_output.txt" | wc -l)
        TESTS_PASSED=$(grep -o "\[.*tests successful.*\]" "$REPORTS_DIR/all_tests_output.txt" | sed 's/\[[ ]*\([0-9]*\).*/\1/')
        TESTS_FAILED=$(grep -o "\[.*tests failed.*\]" "$REPORTS_DIR/all_tests_output.txt" | sed 's/\[[ ]*\([0-9]*\).*/\1/')
        
        # Set default values if empty
        TESTS_PASSED=${TESTS_PASSED:-0}
        TESTS_FAILED=${TESTS_FAILED:-0}
        
        echo -e "${GREEN}✅ Tests passed: $TESTS_PASSED${NC}"
        if [ "$TESTS_FAILED" -gt 0 ]; then
            echo -e "${RED}❌ Tests failed: $TESTS_FAILED${NC}"
        fi
        
        # Show any failures
        if grep -q "FAILED" "$REPORTS_DIR/all_tests_output.txt"; then
            echo -e "\n${RED}❌ Failed Tests:${NC}"
            grep "FAILED" "$REPORTS_DIR/all_tests_output.txt" | head -10
        fi
    fi
    
    echo -e "\n${BLUE}📁 Reports saved to: $REPORTS_DIR${NC}"
}

# Function to run tests by tag
run_tag_tests() {
    local tag="$1"
    echo -e "${BLUE}🏷️  Running tests with tag: $tag${NC}"
    
    java -cp "$CLASSPATH" \
        -Djava.awt.headless=true \
        -Dtestfx.robot=glass \
        -Dtestfx.headless=true \
        -Dprism.order=sw \
        -Dprism.text=t2k \
        -Dglass.platform=Monocle \
        -Dmonocle.platform=Headless \
        -Dprism.verbose=true \
        -Dtest.mode=true \
        org.junit.platform.console.ConsoleLauncher \
        --class-path="$CLASSPATH" \
        --scan-class-path="$TEST_BIN_DIR" \
        --include-tag="$tag" \
        --details=tree \
        --reports-dir="$REPORTS_DIR" \
        2>&1 | tee "$REPORTS_DIR/${tag}_tests_output.txt"
    
    return ${PIPESTATUS[0]}
}

# Function to run unit tests only (fast)
run_unit_tests() {
    echo -e "${GREEN}⚡ Running unit tests (no JavaFX dependencies)${NC}"
    
    java -cp "$CLASSPATH" \
        -Dtest.mode=true \
        org.junit.platform.console.ConsoleLauncher \
        --class-path="$CLASSPATH" \
        --scan-class-path="$TEST_BIN_DIR" \
        --include-tag="unit" \
        --details=tree \
        --reports-dir="$REPORTS_DIR" \
        2>&1 | tee "$REPORTS_DIR/unit_tests_output.txt"
    
    return ${PIPESTATUS[0]}
}

# Function to run integration tests (with JavaFX)
run_integration_tests() {
    echo -e "${YELLOW}🔗 Running integration tests (with JavaFX mocking)${NC}"
    
    java -cp "$CLASSPATH" \
        -Djava.awt.headless=true \
        -Dtestfx.robot=glass \
        -Dtestfx.headless=true \
        -Dprism.order=sw \
        -Dprism.text=t2k \
        -Dglass.platform=Monocle \
        -Dmonocle.platform=Headless \
        -Dprism.verbose=true \
        -Dtest.mode=true \
        org.junit.platform.console.ConsoleLauncher \
        --class-path="$CLASSPATH" \
        --scan-class-path="$TEST_BIN_DIR" \
        --include-tag="integration" \
        --details=tree \
        --reports-dir="$REPORTS_DIR" \
        2>&1 | tee "$REPORTS_DIR/integration_tests_output.txt"
    
    return ${PIPESTATUS[0]}
}

# Parse command line arguments
case "${1:-all}" in
    "all")
        run_all_tests
        TEST_RESULT=$?
        ;;
    "unit")
        run_unit_tests
        TEST_RESULT=$?
        ;;
    "integration")
        run_integration_tests
        TEST_RESULT=$?
        ;;
    "logic")
        run_tag_tests "logic"
        TEST_RESULT=$?
        ;;
    "entities")
        run_package_tests "rougelike.game.entities"
        TEST_RESULT=$?
        ;;
    "game")
        run_package_tests "rougelike.game"
        TEST_RESULT=$?
        ;;
    "network")
        run_package_tests "rougelike.networking"
        TEST_RESULT=$?
        ;;
    "server")
        run_package_tests "Serverapp"
        TEST_RESULT=$?
        ;;
    "model")
        run_package_tests "rougelike"
        TEST_RESULT=$?
        ;;
    "--help" | "-h")
        echo "🧪 Roguelike Game Test Runner"
        echo ""
        echo "Usage: $0 [option]"
        echo ""
        echo "Options:"
        echo "  all          Run all tests (default)"
        echo "  unit         Run unit tests only (fast, no JavaFX)"
        echo "  integration  Run integration tests (with JavaFX mocking)"
        echo "  logic        Run pure logic tests only"
        echo "  entities     Run entity tests only"
        echo "  game         Run game logic tests only"
        echo "  network      Run networking tests only"
        echo "  server       Run server tests only"
        echo "  model        Run model tests only"
        echo "  --help       Show this help message"
        echo ""
        echo "Test Categories:"
        echo "  unit         - Pure logic tests, no JavaFX dependencies (fastest)"
        echo "  integration  - Tests with mocked JavaFX components"
        echo "  logic        - Business logic tests using TestEntity hierarchy"
        echo ""
        echo "Examples:"
        echo "  $0               # Run all tests"
        echo "  $0 unit          # Run only fast unit tests"
        echo "  $0 logic         # Run only pure logic tests"
        echo "  $0 integration   # Run only integration tests"
        echo "  $0 entities      # Run only entity-related tests"
        exit 0
        ;;
    *)
        # Try to run as specific test class
        TEST_CLASS="$1"
        if find "$TEST_BIN_DIR" -name "${TEST_CLASS}*.class" | grep -q .; then
            FULL_CLASS_NAME=$(find "$TEST_BIN_DIR" -name "${TEST_CLASS}*.class" | head -1 | sed "s|$TEST_BIN_DIR/||" | sed 's|\.class||' | sed 's|/|.|g')
            run_test_class "$FULL_CLASS_NAME"
            TEST_RESULT=$?
        else
            echo -e "${RED}❌ Unknown option or test class: $1${NC}"
            echo -e "${YELLOW}💡 Use --help to see available options${NC}"
            exit 1
        fi
        ;;
esac

# Display summary
display_summary

# Exit with test result
if [ $TEST_RESULT -eq 0 ]; then
    echo -e "\n${GREEN}🎉 All tests passed!${NC}"
else
    echo -e "\n${RED}💥 Some tests failed!${NC}"
    echo -e "${YELLOW}💡 Check the reports in $REPORTS_DIR for details${NC}"
fi

exit $TEST_RESULT