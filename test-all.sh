#\!/bin/bash

# Roguelike Game Complete Test Suite
# Compiles and runs all tests in one command

echo "🎯 Roguelike Game - Complete Test Suite"
echo "========================================"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
NC='\033[0m' # No Color

# Check if we're in the right directory
if [ \! -f "test-compile.sh" ] || [ \! -f "test-run.sh" ]; then
    echo -e "${RED}❌ Error: Test scripts not found. Please run from the project root directory.${NC}"
    exit 1
fi

# Step 1: Compile tests
echo -e "${BLUE}Step 1: Compiling tests...${NC}"
echo "----------------------------------------"
./test-compile.sh

if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Test compilation failed. Aborting test run.${NC}"
    exit 1
fi

echo ""

# Step 2: Run tests
echo -e "${BLUE}Step 2: Running tests...${NC}"
echo "----------------------------------------"
./test-run.sh "$@"

TEST_RESULT=$?

echo ""
echo -e "${PURPLE}========================================${NC}"

if [ $TEST_RESULT -eq 0 ]; then
    echo -e "${GREEN}🎉 COMPLETE TEST SUITE PASSED\! 🎉${NC}"
    echo -e "${GREEN}All tests compiled and executed successfully.${NC}"
else
    echo -e "${RED}💥 SOME TESTS FAILED\! 💥${NC}"
    echo -e "${YELLOW}Check the test reports for detailed information.${NC}"
fi

echo -e "${PURPLE}========================================${NC}"

exit $TEST_RESULT
EOF < /dev/null