from Constants import Constants
from Random import Random
class CompanyProblem:

    def __init__ (self, J):
        """
        Generate a random instance of the problem based on an input number of clients J.

        Parameters
        ----------
        J : int (positive)
            Number of clients

        Returns
        -------
        int
            Description of anonymous integer return value.
        """
        J = int(J)
        self._checkInput(J)
        self._setSizes(J)
        self._setParameters()

    def _checkInput(self, J):
        if (J <= 0):
            raise Exception(f"Input value J must be positive. Value provided: '{J}'")

    def _setSizes(self, J):
        self.J = J
        self.F = Random.randint(J, 2*J)
        self.L = Random.randint(Constants.L.low, Constants.L.high)
        self.M = Random.randint(Constants.M.low, Constants.M.high)
        self.P = Random.randint(Constants.P.low, Constants.P.high)

    def _setParameters(self):
        self.D = Random.randArray(self.J, self.P,         low=Constants.D.low, high=Constants.D.high)
        self.r = Random.randArray(self.M, self.P, self.L, low=Constants.r.low, high=Constants.r.high)
        self.R = Random.randArray(self.M, self.F,         low=Constants.R.low, high=Constants.R.high)
        self.C = Random.randArray(self.L, self.F,         low=Constants.C.low, high=Constants.C.high)
        self.p = Random.randArray(self.P, self.L, self.F, low=Constants.p.low, high=Constants.p.high)
        self.t = Random.randArray(self.P, self.F, self.J, low=Constants.t.low, high=Constants.t.high)
