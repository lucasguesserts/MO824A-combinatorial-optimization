# How to create TTT plot

1. select a metaheuristic algorithm `A`
2. select an intance `I` of the problem
3. select a target value `t`
   1. `t <= OPT(I)` for maximization problems
   2. `t >= OPT(I)` for minimization problems
4. run `A(I)` `n` times and save the running times `<T_1, ..., T__n>`
5. make the TTT (time-to-target) plot `TTT(<T_1, ..., T__n>)`
