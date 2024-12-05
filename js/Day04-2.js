const fs = require('fs');

const loadMatrixFromFile = (filePath) => {
  const data = fs.readFileSync(filePath, 'utf8');
  const rows = data.split('\n');
  return rows.map(row => row.split(''));
};

const matrix = loadMatrixFromFile("src/main/resources/day04.txt");

const reverseMatrix = (m) => m.map(row => row.slice().toReversed())

const getDiagonal = (matrix) => {
    const flatArray = matrix.flat()
    const diagonals = []

    for (let i = 0; i < matrix.length * matrix.length; i += matrix.length)  {
        const row = []
        for (let j = i; j < matrix.length * matrix.length; j += matrix.length + 1) {
            row.push(j)
        }
        diagonals.push(row)
    }

    for (let i = matrix.length * matrix.length - 1 - matrix.length; i > 0; i -= matrix.length)  {
        const row = []
        for (let j = i; j > 0; j -= matrix.length + 1) {
            row.push(j)
        }
        diagonals.push(row)
    }

    return diagonals;
}

const findOccurrencesInMatrix = (matrix, original) => {
        const flat = original.flat()
        const regex = /(?=(MAS|SAM))/g;
        return matrix.reduce((matchIndex, next) => {
            const matches = [...next.map(i => flat[i]).join('').matchAll(regex)].map(m => m.index + 1);
            if (matches.length) {
                return [...matchIndex, ...matches.map(m => next[m])]
            }
            return matchIndex
        }, [])
    }

const matrixMirrored = reverseMatrix(matrix)
const primaryDiagonal = getDiagonal(matrix)
const secondaryDiagonal = getDiagonal(matrixMirrored)


const primeryMatches = findOccurrencesInMatrix(primaryDiagonal, matrix)
const secondaryMatches = findOccurrencesInMatrix(secondaryDiagonal, matrixMirrored)


function getOriginalFlattenedIndex(flatIndex) {
  const row = Math.floor(flatIndex / matrix.length);
  const col = matrix.length - 1 - (flatIndex % matrix.length);
  return row * matrix.length + col;
}
const result = secondaryMatches.map(getOriginalFlattenedIndex).filter(s => primeryMatches.includes(s))
console.log(result.length)

