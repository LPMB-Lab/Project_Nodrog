/*
 *   FINGERS:
 *   ==========
 *   LEFT HAND:
 *   -4 : Pinky
 *   -3 : Ring
 *   -2 : Middle
 *   -1 : Index
 *   
 *   RIGHT HAND:
 *   +1 : Index
 *   +2 : Middle
 *   +3 : Ring
 *   +4 : Pinky
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Trial
{
	int[] m_aEntries = new int[20];
	int[] m_aTimers = new int[20];
	int[] m_aIntraMirror = new int[4];
	
	boolean m_bHasBeenCompleted;
	int m_iIntraSwapMirrorCount;
	
	Trial()
	{
		m_bHasBeenCompleted = false;
		
		for (int i = 0; i < m_aEntries.length; i++)
			m_aTimers[i] = 0;
		
		m_iIntraSwapMirrorCount = 0;
		GenerateRawTrial();
		GenerateTrial();
	}
	public int getCurrentFinger(int index)
	{
		return m_aEntries[index];
	}
	private void GenerateRawTrial()
	{
		ArrayList<Integer> MirrorSwapNumbers = new ArrayList<Integer>();
		ArrayList<Integer> EntryNumbers = new ArrayList<Integer>();
		
		for (int i = 1; i < 9; i++)
			MirrorSwapNumbers.add(i);
		
		for (int i = 1; i < 9; i++)
			EntryNumbers.add(0);
		
		for (int i = 1; i < 12; i++)
			EntryNumbers.add(1);
			
		Collections.shuffle(EntryNumbers);
		Collections.shuffle(MirrorSwapNumbers);
		
		for (int i = 0; i < m_aIntraMirror.length; i++)
			m_aIntraMirror[i] = MirrorSwapNumbers.get(i);
		
		EntryNumbers.add(0, 0);
		
		for (int i = 0; i < m_aEntries.length; i++)
			m_aEntries[i] = EntryNumbers.get(i);
	}
	private void GenerateTrial()
	{		
		for (int i = 0; i < m_aEntries.length; i++)
		{
			if (i == 0)
			{
				if (new Random().nextInt(2) == 0)
					m_aEntries[i] = RandomFingerRight(0);
				else m_aEntries[i] = RandomFingerLeft(0);
			}
			else
			{
				if (isLeftHand(m_aEntries[i-1]))
				{
					if (isInterSwap(m_aEntries[i]))
					{
						m_iIntraSwapMirrorCount++;
						boolean isMirror = false;
						
						for (int j = 0; j < m_aIntraMirror.length; j++)
						{
							if (m_aIntraMirror[j] == m_iIntraSwapMirrorCount)
							{
								m_aEntries[i] = m_aEntries[i-1]*-1;
								isMirror = true;
							}
						}
						
						if (!isMirror)
							m_aEntries[i] = RandomFingerRight(m_aEntries[i-1]);
					}
					else
						m_aEntries[i] = RandomFingerLeft(m_aEntries[i-1]);
				}
				else
				{
					if (isInterSwap(m_aEntries[i]))
					{
						m_iIntraSwapMirrorCount++;
						boolean isMirror = false;
						
						for (int j = 0; j < m_aIntraMirror.length; j++)
						{
							if (m_aIntraMirror[j] == m_iIntraSwapMirrorCount)
							{
								m_aEntries[i] = m_aEntries[i-1]*-1;
								isMirror = true;
							}
						}
						
						if (!isMirror)
							m_aEntries[i] = RandomFingerLeft(m_aEntries[i-1]);
					}
					else
						m_aEntries[i] = RandomFingerRight(m_aEntries[i-1]);
				}
			}
		}
	}
	private int RandomFingerRight(int previousID)
	{
		int newFinger;
		
		if (previousID < 0)
			previousID *= -1;
		
		do	newFinger = new Random().nextInt(4)+1;
		while (newFinger == previousID);
		
		return newFinger;
	}
	private int RandomFingerLeft(int previousID)
	{
		int newFinger;
		
		if (previousID < 0)
			previousID *= -1;
		
		do	newFinger = (new Random().nextInt(4)+1);	
		while (newFinger == previousID);
		
		return newFinger*-1;
	}
	private boolean isLeftHand(int id)	{ return id < 0 ? true : false; }
	private boolean isInterSwap(int id)	{ return id == 0 ? true : false; }
}