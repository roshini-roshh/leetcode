public String convert(String s, int numRows) {
    // Edge cases where zigzag shape isn't possible
    if (numRows == 1 || numRows >= s.length()) {
        return s;
    }
    
    // Initialize StringBuilder for each row
    StringBuilder[] rows = new StringBuilder[numRows];
    for (int i = 0; i < numRows; i++) {
        rows[i] = new StringBuilder();
    }
    
    int currentRow = 0;
    boolean goingDown = false;
    
    for (char c : s.toCharArray()) {
        rows[currentRow].append(c);
        
        // Turn around at the boundaries
        if (currentRow == 0 || currentRow == numRows - 1) {
            goingDown = !goingDown;
        }
        
        // Move up or down based on current direction
        currentRow += goingDown ? 1 : -1;
    }
    
    // Combine all rows into a single string
    StringBuilder result = new StringBuilder();
    for (StringBuilder row : rows) {
        result.append(row);
    }
    
    return result.toString();
}
