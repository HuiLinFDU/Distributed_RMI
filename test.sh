#!/bin/bash

#=== Configuration
LOG_FILE="TestLog.txt"
CLASS_PATH="demo/target/classes:demo/lib/sqlite-jdbc.jar:."
DB_FILE="$PWD/CSCI7785_database.db"

#=== Enhanced logging
exec 3>&1  
log() {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1" | tee -a "$LOG_FILE" >&3
}

#=== Cleanup 
cleanup() {
    pkill -P $$ 2>/dev/null  # Kill all child processes
    kill $SERVER_PID 2>/dev/null
    wait 2>/dev/null
}
trap cleanup EXIT TERM INT

#=== Initialize
> "$LOG_FILE"
if [ ! -f "$DB_FILE" ]; then
    log "------ Initializing new database..."
    touch "$DB_FILE"
fi

#=== Start RMI Server
log "------ Starting RMI Server..."
java -cp "$CLASS_PATH" com.example.RMIServer >> "$LOG_FILE" 2>&1 &
SERVER_PID=$!
sleep 5  #Wait for server ready

#=== Basic CRUD Tests
run_basic_tests() {
    log "\n=== Running Basic CRUD Tests ==="
    
    # Test 1: List existed employees
    log "------ Test 1: List all employees"
    run_client_command $'1\n6' "TEST1_LIST"
    
    # Test 2: Add new employees
    log "------ Test 2: Add new employees"
    run_client_command $'3\nE123\nDonald Trump\nPresident\n6' "TEST2_ADD1"
    run_client_command $'3\nE124\nElon Musk\nMinister\n6' "TEST2_ADD1"
    run_client_command $'1\n6' "TEST2_LIST"
    
    # Tests 3: Find specific employee
    log "------ Test 3: Find employee"
    run_client_command $'2\nE124\n6' "TEST3_FIND"
    
    # Tests 4: Update specific employee
    log "------ Test 4: Update employee"
    run_client_command $'5\nE124\nElon Musk\nStaff\n6' "TEST4_UPDT"
    run_client_command $'2\nE124\n6' "TEST4_LIST"
    
    # Tests 5: Delete specific employee
    log "------ Test 5: Delete employee"
    run_client_command $'4\nE124\n6' "TEST5_DELT"
    run_client_command $'1\n6' "TEST5_LIST"
}

#=== Concurrent Test Functions
run_concurrent_tests() {
    log "\n=== Running Concurrent Tests ==="
    
    # Prepare test record
    run_client_command $'3\nE007\nJames Bond\nAgent\n6' "CT1_PRESET"
    
    # PROC1: Long transaction (update + delay)
    (
        log "[CT1_TX] Starting long transaction"
        run_client_command $'5\nE007\nJason Bourne\nHitman\n6' "CT1_UPD" &
        PROC1_PID=$!
        sleep 2  # Simulate long operation
        wait $PROC1_PID
    ) &
    
    # PROC2: Concurrent read 
    (
        sleep 1
        log "[CT2_READ] Attempting concurrent read"
        run_client_command $'2\nE007\n6' "CT2_READ"
    ) &
    
    # PROC3: Concurrent write 
    (
        sleep 1.5
        log "[CT3_WRITE] Attempting concurrent write"
        run_client_command $'5\nE007\nJames Bond\nStaff\n6' "CT3_WRITE"
    ) &
    
    wait  # Wait for all concurrent tests
    log "Concurrent tests completed"
}

#=== Helper function to run client commands with timeout
run_client_command() {
    local input=$1
    local tag=$2
    local timeout=8  # Max execution time
    
    log "[$tag] Executing command"
    (
        java -cp "$CLASS_PATH" com.example.EmpDBConsoleApp <<< "$input" | 
        awk -v tag="$tag" '{print "["tag"] " $0}'
    ) >> "$LOG_FILE" 2>&1 &
    
    local pid=$!
    ( sleep $timeout && kill $pid 2>/dev/null && 
      log "[$tag] WARNING: Operation timed out after ${timeout}s" ) &
    local killer_pid=$!
    
    wait $pid 2>/dev/null
    kill $killer_pid 2>/dev/null
}

#=== Main execution
log "RMI Server started (PID: $SERVER_PID)"
run_basic_tests
run_concurrent_tests

#=== Final verification
log "\n=== Final Verification ==="
run_client_command $'1\n6' "FINAL_LIST"

log "\n=== All Tests Completed ==="
echo "Detailed log available at: $LOG_FILE" >&3