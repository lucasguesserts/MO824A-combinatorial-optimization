const fs = require('fs')

let files = fs.readdirSync('./')
files = files.filter(f => f.includes('.jpeg'))
const cases = Array.from(new Set(files.map(
    fileName => fileName
        .replace('-exp','')
        .replace('-qq','')
        .replace('.jpeg','')
)))

// const names = Array.from(new Set(cases.map(
//     c => c.replace(/kqbf([0-9])+-/,'')
// )))
// console.log(names)

const nameMap = {
    'genetic-steady-100-1': 'GA steady',
    'genetic-vanilla-100-05': 'GA vanilla',
    'grasp-best': 'GRASP Best',
    'grasp-first': 'GRASP First',
    'tabu-best-vanilla-4': 'Tabu vanilla',
    'tabu-diversification': 'Tabu com Intensificação e Diversificação',
}

function getName (c) {
    const pureCaseName = c.replace(/kqbf([0-9])+-/,'')
    const problemInstanceName = c.match(/kqbf([0-9])+/)[0]
    return {
        algorithm: nameMap[pureCaseName],
        problemInstanceName: problemInstanceName
    }

}

const latexFigures = cases.map(c => {
    const names = getName(c)
    return `
\\begin{figure}[H]
    \\centering
    \\begin{subfigure}{0.49\\textwidth}
        \\includegraphics[width=\\textwidth]{figure/ttt_plot/${c}-exp.jpeg}
        \\caption{Cumulative Probability Distribution - Algorithm ${names.algorithm} - Problem ${names.problemInstanceName}}
        \\label{fig:${names.algorithm.toLowerCase().replace(' ','-')}-${names.problemInstanceName}-exp}
    \\end{subfigure}
    \\hfill
    \\begin{subfigure}{0.49\\textwidth}
        \\includegraphics[width=\\textwidth]{figure/ttt_plot/${c}-qq.jpeg}
        \\caption{Q-Q plot - Algorithm ${names.algorithm} - Problem ${names.problemInstanceName}}
        \\label{fig:${names.algorithm.toLowerCase().replace(' ','-')}-${names.problemInstanceName}-qq}
    \\end{subfigure}
    \\caption{Algorithm ${names.algorithm} - Problem ${names.problemInstanceName}.}
    \\label{fig:${names.algorithm.toLowerCase().replace(' ','-')}-${names.problemInstanceName}}
\\end{figure}
`
})

const allFiguresTogether =
    '\\section{\\tttfull (\\ttt)}\n' +
    latexFigures.join('\n')

fs.writeFileSync(
    '../../appendix/ttt_plot.tex',
    allFiguresTogether
)
