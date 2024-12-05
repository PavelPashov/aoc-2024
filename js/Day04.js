const fs = require('fs');

const loadMatrixFromFile = (filePath) => {
  const data = fs.readFileSync(filePath, 'utf8');
  const rows = data.split('\n');
  return rows.map(row => row.split(''));
};

const matrix = loadMatrixFromFile("src/main/resources/day04.txt");

const rotateUp = (matrix) => {
    const flat = matrix.flat()
    const newMatrix = []
    for (let i = 0; i < matrix.length; i +=1)  {
            const row = []
            for (let j = i; j < matrix.length * matrix.length; j += matrix.length) {
                row.push(flat[j])
            }
            newMatrix.push(row)
        }
    return newMatrix;
}

const reverseMatrix = (m) => m.map(row => row.slice().toReversed())

const getDiagonal = (matrix) => {
    const flatArray = matrix.flat()
    const diagonals = []

    for (let i = 0; i < matrix.length * matrix.length; i += matrix.length)  {
        const row = []
        for (let j = i; j < matrix.length * matrix.length; j += matrix.length + 1) {
            row.push(flatArray[j])
        }
        diagonals.push(row)
    }

    for (let i = matrix.length * matrix.length - 1 - matrix.length; i > 0; i -= matrix.length)  {
        const row = []
        for (let j = i; j > 0; j -= matrix.length + 1) {
            row.push(flatArray[j])
        }
        diagonals.push(row)
    }

    return diagonals;
}

const findOccurrencesInMatrix = (matrix) => {
        const regex = /XMAS/g;
        return matrix.reduce((sum, next) => {
            const matches = next.join('').match(regex)
            return matches ? sum + matches.length : sum
        }, 0)
    }

const matrixMirrored = reverseMatrix(matrix)
const rotated  = rotateUp(matrix);
const rotatedMirrored = reverseMatrix(rotated)
const primaryDiagonal = getDiagonal(matrix)
const secondaryDiagonal = getDiagonal(matrixMirrored)
const primaryDiagonalMirrored = reverseMatrix(primaryDiagonal)
const secondaryDiagonalMirrored = reverseMatrix(secondaryDiagonal)

const result = findOccurrencesInMatrix(matrix)
+ findOccurrencesInMatrix(matrixMirrored)
+ findOccurrencesInMatrix(rotated)
+ findOccurrencesInMatrix(rotatedMirrored)
+ findOccurrencesInMatrix(primaryDiagonal)
+ findOccurrencesInMatrix(primaryDiagonalMirrored)
+ findOccurrencesInMatrix(secondaryDiagonal)
+ findOccurrencesInMatrix(secondaryDiagonalMirrored)

console.log(result)
